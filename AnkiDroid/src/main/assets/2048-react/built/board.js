"use strict";

var rotateLeft = function rotateLeft(matrix) {
  var rows = matrix.length;
  var columns = matrix[0].length;
  var res = [];
  for (var row = 0; row < rows; ++row) {
    res.push([]);
    for (var column = 0; column < columns; ++column) {
      res[row][column] = matrix[column][columns - row - 1];
    }
  }
  return res;
};

var Tile = function Tile(value, row, column) {
  this.value = value || 0;
  this.row = row || -1;
  this.column = column || -1;
  this.oldRow = -1;
  this.oldColumn = -1;
  this.markForDeletion = false;
  this.mergedInto = null;
  this.id = Tile.id++;
};

Tile.id = 0;

Tile.prototype.moveTo = function (row, column) {
  this.oldRow = this.row;
  this.oldColumn = this.column;
  this.row = row;
  this.column = column;
};

Tile.prototype.isNew = function () {
  return this.oldRow == -1 && !this.mergedInto;
};

Tile.prototype.hasMoved = function () {
  return this.fromRow() != -1 && (this.fromRow() != this.toRow() || this.fromColumn() != this.toColumn()) || this.mergedInto;
};

Tile.prototype.fromRow = function () {
  return this.mergedInto ? this.row : this.oldRow;
};

Tile.prototype.fromColumn = function () {
  return this.mergedInto ? this.column : this.oldColumn;
};

Tile.prototype.toRow = function () {
  return this.mergedInto ? this.mergedInto.row : this.row;
};

Tile.prototype.toColumn = function () {
  return this.mergedInto ? this.mergedInto.column : this.column;
};

var Board = function Board(state, bestScore, history) {
  this.tiles = [];
  this.cells = [];
  for (var i = 0; i < Board.size; ++i) {
    this.cells[i] = [this.addTile(), this.addTile(), this.addTile(), this.addTile()];
  }

  if (state) {
    this.setTileValues(state.values);
    this.score = state.score;
    this.usedTricks = state.usedTricks;
  } else {
    this.addRandomTile();
    this.addRandomTile(); // Two tiles at the begining
    this.score = 0;
    this.usedTricks = [];
  }

  if (bestScore) {
    this.bestScore = bestScore;
  } else {
    this.bestScore = 0;
  }

  if (history) {
    this.history = history;
  } else {
    this.history = [];
  }

  this.setPositions();
  this.addition = 0;
  this.won = false;
  this.continueAfterWon = false;
};

Board.prototype.setTileValues = function (values) {
  // values is an array of size 16
  var self = this;
  this.cells.forEach(function (row, rowIndex) {
    row.forEach(function (tile, columnIndex) {
      tile.value = values[rowIndex * Board.size + columnIndex];
    });
  });
};

Board.prototype.addTile = function () {
  var res = new Tile();
  Tile.apply(res, arguments);
  this.tiles.push(res);
  return res;
};

Board.size = 4;

Board.prototype.moveLeft = function () {
  var hasChanged = false;
  this.addition = 0;
  for (var row = 0; row < Board.size; ++row) {
    // A row has to be processed to merge the colliding tiles
    // Those non zero tiles are the ones to collide
    var currentRow = this.cells[row].filter(function (tile) {
      return tile.value != 0;
    });
    var resultRow = [];

    for (var target = 0; target < Board.size; ++target) {
      var targetTile = currentRow.length ? currentRow.shift() : this.addTile();

      if (currentRow.length > 0) {
        if (currentRow[0].value == targetTile.value || currentRow[0].value == -1 || targetTile.value == -1) {

          var tile1 = targetTile;
          targetTile = this.addTile(targetTile.value);
          tile1.mergedInto = targetTile;

          var tile2 = currentRow.shift();
          tile2.mergedInto = targetTile;

          var toAdd = 0;
          if (this.isGift(targetTile, tile2)) {
            targetTile.value = this.mergeGift(targetTile, tile2);
            toAdd = targetTile.value > 0 ? targetTile.value : 0;
          } else {
            targetTile.value += tile2.value;
            toAdd = targetTile.value;
          }
          this.addition += toAdd;
        }
      }
      resultRow[target] = targetTile;
      this.won |= targetTile.value == 2048;
      hasChanged |= targetTile.value != this.cells[row][target].value;
    }
    this.cells[row] = resultRow;
  }

  this.score += this.addition;
  if (this.score > this.bestScore) {
    this.bestScore = this.score;
    Anki.updateBestScore(this.bestScore);
  }
  if (this.won && !this.continueAfterWon) {
    Anki.hasWon(this.asString());
  }
  return hasChanged;
};

Board.prototype.isGift = function (tile1, tile2) {
  var result = false;
  if (tile1.value == -1 && tile2.value > 0) {
    result = true;
  } else if (tile1.value > 0 && tile2.value == -1) {
    result = true;
  } else if (tile1.value == -1 && tile2.value == -1) {
    result = true;
  }
  return result;
};

Board.prototype.mergeGift = function (tile1, tile2) {
  var result = 0;

  if (tile1.value == -1 && tile2.value == -1) {
    result = -1;
  } else if (tile1.value == -1) {
    result = tile2.value < 512 ? tile2.value * 2 : tile2.value;
  } else if (tile2.value == -1) {
    result = tile1.value < 512 ? tile1.value * 2 : tile1.value;
  } else {
    // unreachable
    result = tile1.value + tile2.value;
  }

  return result;
};

Board.prototype.setPositions = function () {
  this.cells.forEach(function (row, rowIndex) {
    row.forEach(function (tile, columnIndex) {
      tile.oldRow = tile.row;
      tile.oldColumn = tile.column;
      tile.row = rowIndex;
      tile.column = columnIndex;
      tile.markForDeletion = false;
    });
  });
};

Board.fourProbability = 0.1;

Board.prototype.addRandomTile = function () {
  var value = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 2;

  var emptyCells = [];
  for (var r = 0; r < Board.size; ++r) {
    for (var c = 0; c < Board.size; ++c) {
      if (this.cells[r][c].value == 0) {
        emptyCells.push({ r: r, c: c });
      }
    }
  }
  var index = ~~(Math.random() * emptyCells.length);
  var cell = emptyCells[index];
  //var newValue = Math.random() < Board.fourProbability ? 4 : 2;
  var newValue = value;
  this.cells[cell.r][cell.c] = this.addTile(newValue);
};

Board.prototype.move = function (direction) {
  // For history.
  // The state has to be stored once the board has changed.
  var state = this.serialize();

  // 0 -> left, 1 -> up, 2 -> right, 3 -> down
  this.clearOldTiles();
  for (var i = 0; i < direction; ++i) {
    this.cells = rotateLeft(this.cells);
  }
  var hasChanged = this.moveLeft();
  for (var i = direction; i < 4; ++i) {
    this.cells = rotateLeft(this.cells);
  }
  if (hasChanged) {
    this.addRandomTile();
    this.updateHistory(state);
  }
  this.setPositions();
  return this;
};

Board.prototype.clearOldTiles = function () {
  this.tiles = this.tiles.filter(function (tile) {
    return tile.markForDeletion == false;
  });
  this.tiles.forEach(function (tile) {
    tile.markForDeletion = true;
  });
};

Board.deltaX = [-1, 0, 1, 0];
Board.deltaY = [0, -1, 0, 1];

// Board to test lost
// [2,2,8,16,32,64,128,256,512,128,64,32,16,8,4,2]
Board.prototype.hasLost = function () {
  var canMove = false;
  for (var row = 0; row < Board.size; ++row) {
    for (var column = 0; column < Board.size; ++column) {
      canMove |= this.cells[row][column].value == 0;
      for (var dir = 0; dir < 4; ++dir) {
        var newRow = row + Board.deltaX[dir];
        var newColumn = column + Board.deltaY[dir];
        if (newRow < 0 || newRow >= Board.size || newColumn < 0 || newColumn >= Board.size) {
          continue;
        }
        canMove |= this.cells[row][column].value == this.cells[newRow][newColumn].value;
      }
    }
  }
  return !canMove;
};

Board.prototype.removeTwos = function () {
  // For history.
  // The state has to be stored once the board has changed.
  var state = this.serialize();

  this.clearOldTiles();
  var hasChanged = false;
  for (var r = 0; r < Board.size; ++r) {
    for (var c = 0; c < Board.size; ++c) {
      if (this.cells[r][c].value == 2) {
        this.cells[r][c] = this.addTile();
        this.cells[r][c].markForDeletion = true;
        hasChanged = true;
      }
    }
  }
  this.setPositions();

  // Need to clear old tile since those with value 2
  // are supposed to be removed
  this.clearOldTiles();
  this.setPositions();

  // Add the trick to the list of used ones
  if (hasChanged) {
    this.usedTricks.push("bomb");
    this.updateHistory(state);
  }
  return this;
};

// At least one cell has to be greater than two
Board.prototype.ableToDeleteTwos = function () {
  var atLeastOneTwo = false;
  var atLeastOneGreaterThanTwo = false;
  for (var r = 0; r < Board.size; ++r) {
    for (var c = 0; c < Board.size; ++c) {
      var value = this.cells[r][c].value;
      if (value == 2) {
        atLeastOneTwo = true;
      } else if (value > 2) {
        atLeastOneGreaterThanTwo = true;
      }
    }
  }
  return atLeastOneTwo && atLeastOneGreaterThanTwo;
};

Board.prototype.addGift = function () {
  // For history.
  // The state has to be stored once the board has changed.
  var state = this.serialize();

  var hasChanged = false;
  if (this.hasEmptyCells()) {
    hasChanged = true;
    this.clearOldTiles();
    this.addRandomTile(-1);
    this.setPositions();
  }

  if (hasChanged) {
    // Add the trick to the list of used ones
    this.usedTricks.push("gift");
    this.updateHistory(state);
  }
  return this;
};

Board.prototype.double = function () {
  // For history.
  // The state has to be stored once the board has changed.
  var state = this.serialize();

  this.clearOldTiles();
  var hasChanged = false;
  for (var r = 0; r < Board.size; ++r) {
    for (var c = 0; c < Board.size; ++c) {
      var value = this.cells[r][c].value;
      if (value > 0 && value <= 2) {
        this.cells[r][c].value *= 2;
        this.cells[r][c].markForDeletion = false;
        hasChanged = true;
      }
    }
  }

  // Add the trick to the list of used ones
  if (hasChanged) {
    var values = this.serializeTiles();
    this.setGridState(values);

    this.usedTricks.push("double");
    this.updateHistory(state);
  }
  return this;
};

Board.prototype.ableToDouble = function () {
  for (var r = 0; r < Board.size; ++r) {
    for (var c = 0; c < Board.size; ++c) {
      var value = this.cells[r][c].value;
      if (value > 0 && value <= 2) {
        return true;
      }
    }
  }
  return false;
};

Board.prototype.hasEmptyCells = function () {
  for (var r = 0; r < Board.size; ++r) {
    for (var c = 0; c < Board.size; ++c) {
      if (this.cells[r][c].value == 0) {
        return true;
      }
    }
  }
  return false;
};

Board.prototype.serialize = function () {
  return {
    usedTricks: this.usedTricks,
    hasLost: this.hasLost(),
    bestScore: this.bestScore,
    score: this.score,
    values: this.serializeTiles()
  };
};

Board.prototype.serializeTiles = function () {
  var values = [];
  this.cells.forEach(function (row, rowIndex) {
    row.forEach(function (tile, columnIndex) {
      values.push(tile.value);
    });
  });

  return values;
};

Board.prototype.asString = function () {
  return JSON.stringify(this.serialize());
};

Board.prototype.updateHistory = function (state) {
  // TODO: AnkiGame, Check the hard-coded value
  if (this.history.length >= 10) {
    this.history.shift();
  }
  this.history.push(state);
};

Board.prototype.undo = function () {
  var last = this.history.pop();
  if (typeof last !== "undefined") {
    this.score = last.score;
    this.bestScore = last.bestScore;
    this.setGridState(last.values);

    // Add the trick to the list of used ones
    this.usedTricks.push("undo");
  }

  return this;
};

Board.prototype.hasHistory = function () {
  return this.history.length > 0;
};

Board.prototype.setGridState = function (tileValues) {
  this.tiles = [];
  this.cells = [];
  for (var i = 0; i < Board.size; ++i) {
    this.cells[i] = [this.addTile(), this.addTile(), this.addTile(), this.addTile()];
  }
  this.setTileValues(tileValues);
  this.setPositions();
};