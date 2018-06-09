'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

// Uncomment just for testing purposes
// var Anki = {
//   hasPointsForTrick: function(requiredPoints) {
//     console.log("required points: " + requiredPoints);
//     return true;
//   },
//   noPointsForTrick: function(trickName) {
//     console.log("no points for trick: " + trickName);
//   },
//   hasCoinsForTrick: function(requiredCoins) {
//     console.log("required coins: " + requiredCoins);
//     return true;
//   },
//   noCoinsForTrick: function(trickName) {
//     console.log("no money for trick: " + trickName);
//   },
//   unableTrick: function(trickName) {
//     console.log("unable to do " + trickName);
//   },
//   doTrick: function(trickName) {
//     console.log("do trick " + trickName)
//   },
//   updateBestScore: function(score) {
//     console.log("best score " + score);
//   },
//   getAnkiPoints: function() {
//     return 50000;
//   },
//   getAnkiCoins: function() {
//     return 20;
//   },
//   hasLost: function() {
//       console.log("lost");
//   },
//   hasWon: function(){
//     console.log("won");
//   }
// }

var BoardView = function (_React$Component) {
  _inherits(BoardView, _React$Component);

  function BoardView(props) {
    _classCallCheck(this, BoardView);

    var _this = _possibleConstructorReturn(this, (BoardView.__proto__ || Object.getPrototypeOf(BoardView)).call(this, props));

    _this.setup();
    return _this;
  }

  _createClass(BoardView, [{
    key: 'setup',
    value: function setup() {
      this.storageManager = new LocalStorageManager();
      var previousState = this.storageManager.getGameState();
      var bestScore = this.storageManager.getBestScore();
      var history = this.storageManager.getHistory();
      this.state = { board: new Board(previousState, bestScore, history) };
      this.points = Anki.getAnkiPoints();
      this.coins = Anki.getAnkiCoins();
      this.tricks = {
        gift: {
          name: 'gift',
          coins: 10,
          points: 100,
          isPermitted: this.state.board.hasEmptyCells,
          execute: this.state.board.addGift
        },
        double: {
          name: 'double',
          coins: 20,
          points: 500,
          isPermitted: this.state.board.ableToDouble,
          execute: this.state.board.double
        },
        bomb: {
          name: 'bomb',
          coins: 30,
          points: 1000,
          isPermitted: this.state.board.ableToDeleteTwos,
          execute: this.state.board.removeTwos
        },
        undo: {
          name: 'undo',
          coins: 40,
          points: 2000,
          isPermitted: this.state.board.hasHistory,
          execute: this.state.board.undo
        }
      };
    }
  }, {
    key: 'restartGame',
    value: function restartGame() {
      // Do not restart the best score
      var bestScore = this.storageManager.getBestScore();
      this.setState({ board: new Board(undefined, bestScore, undefined) });
      this.storageManager.clearGameState();
      this.storageManager.clearHistory();
      this.points = Anki.getAnkiPoints();
      this.coins = Anki.getAnkiCoins();
    }
  }, {
    key: 'getBoardStateAsString',
    value: function getBoardStateAsString() {
      return this.state.board.asString();
    }
  }, {
    key: 'handleKeyDown',
    value: function handleKeyDown(event) {
      if (event.keyCode >= 37 && event.keyCode <= 40) {
        event.preventDefault();
        var direction = event.keyCode - 37;
        this.setState({ board: this.state.board.move(direction) });
        if (this.state.board.hasLost() && !this.ableToDoAnyTrick()) {
          Anki.hasLost();
        }
      }
    }
  }, {
    key: 'handleTouchStart',
    value: function handleTouchStart(event) {
      if (event.touches.length != 1) {
        return;
      }
      this.startX = event.touches[0].screenX;
      this.startY = event.touches[0].screenY;
      event.preventDefault();
    }
  }, {
    key: 'handleTouchEnd',
    value: function handleTouchEnd(event) {
      if (event.changedTouches.length != 1) {
        return;
      }
      var deltaX = event.changedTouches[0].screenX - this.startX;
      var deltaY = event.changedTouches[0].screenY - this.startY;
      var direction = -1;
      if (Math.abs(deltaX) > 3 * Math.abs(deltaY) && Math.abs(deltaX) > 30) {
        direction = deltaX > 0 ? 2 : 0;
      } else if (Math.abs(deltaY) > 3 * Math.abs(deltaX) && Math.abs(deltaY) > 30) {
        direction = deltaY > 0 ? 3 : 1;
      }
      if (direction != -1) {
        this.setState({ board: this.state.board.move(direction) });
        if (this.state.board.hasLost() && !this.ableToDoAnyTrick()) {
          Anki.hasLost();
        }
      }
    }
  }, {
    key: 'componentDidMount',
    value: function componentDidMount() {
      window.addEventListener('keydown', this.handleKeyDown.bind(this));
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      window.removeEventListener('keydown', this.handleKeyDown.bind(this));
    }
    // This function has to be bound when assigning it to the child's
    // prop, otherwise, it won't work.

  }, {
    key: 'tryTrick',
    value: function tryTrick(trickName, event) {
      event.preventDefault();

      var trick = this.tricks[trickName];
      var requiredCoins = trick["coins"];
      var trickName = trick["name"];
      var requiredPoints = trick["points"];
      var isPermitted = trick["isPermitted"];
      var execute = trick["execute"];

      if (!Anki.hasPointsForTrick(requiredPoints)) {
        Anki.noPointsForTrick(trickName, requiredPoints, this.getBoardStateAsString());
        return;
      }

      if (!Anki.hasCoinsForTrick(requiredCoins)) {
        Anki.noCoinsForTrick(trickName, requiredCoins, this.getBoardStateAsString());
        return;
      }

      // Use apply to bind the object
      if (!isPermitted.apply(this.state.board)) {
        Anki.unableTrick(trickName, this.getBoardStateAsString());
        return;
      }
      // TODO: AnkiGame, Executing the double trick can make end the game. Handle that
      Anki.doTrick(trickName, requiredCoins, this.getBoardStateAsString());
      this.setState({ board: execute.apply(this.state.board) });

      // After applying a trick, the number of coins changed. Update the variable
      // since the visual of the tricks depends on that value
      this.coins = Anki.getAnkiCoins();
    }
  }, {
    key: 'ableToDoAnyTrick',
    value: function ableToDoAnyTrick() {
      var tricks = this.tricks;
      var board = this.state.board;
      var points = this.points;
      var coins = this.coins;

      var t = Object.keys(tricks).filter(function (key, index) {
        var trick = tricks[key];

        var requiredPoints = trick['points'];
        var availablePoints = points;
        if (availablePoints < requiredPoints) {
          return false;
        }

        var requiredCoins = trick['coins'];
        var availableCoins = coins;
        if (availableCoins < requiredCoins) {
          return false;
        }

        if (!trick['isPermitted'].apply(board)) {
          return false;
        }

        return true;
      });

      return t.length > 0;
    }
  }, {
    key: 'continuePlaying',
    value: function continuePlaying() {
      this.state.board.continueAfterWon = true;
    }
  }, {
    key: 'render',
    value: function render() {
      // Since render is executed every time the state changes
      // Here we store the state of the game and the best score
      this.storageManager.setGameState(this.state.board.serialize());
      this.storageManager.setBestScore(this.state.board.bestScore);
      this.storageManager.setHistory(this.state.board.history);

      var tricks = this.tricks;
      var currentBoard = this;
      var trickElements = Object.keys(tricks).map(function (key, index) {
        return React.createElement(Trick, { Board: currentBoard, trickName: key });
      });

      var bestScore = this.state.board.bestScore;
      var bestScoreElem = React.createElement(BestScore, { bestScore: bestScore });

      var currentScore = this.state.board.score;
      var addition = this.state.board.addition;
      var scoreElem = React.createElement(Score, { score: currentScore, addition: addition });

      var cells = this.state.board.cells.map(function (row, rowIndex) {
        return React.createElement(
          'div',
          { key: rowIndex },
          row.map(function (_, columnIndex) {
            return React.createElement(Cell, { key: rowIndex * Board.size + columnIndex });
          })
        );
      });
      var tiles = this.state.board.tiles.filter(function (tile) {
        return tile.value != 0;
      }).map(function (tile) {
        return React.createElement(TileView, { tile: tile, key: tile.id });
      });
      return React.createElement(
        'div',
        null,
        React.createElement(
          'div',
          { className: 'scores-container' },
          scoreElem,
          ' ',
          bestScoreElem
        ),
        React.createElement(
          'div',
          { className: 'board', onTouchStart: this.handleTouchStart.bind(this), onTouchEnd: this.handleTouchEnd.bind(this), tabIndex: '1' },
          cells,
          tiles
        ),
        trickElements
      );
    }
  }]);

  return BoardView;
}(React.Component);

;

var Cell = function (_React$Component2) {
  _inherits(Cell, _React$Component2);

  function Cell() {
    _classCallCheck(this, Cell);

    return _possibleConstructorReturn(this, (Cell.__proto__ || Object.getPrototypeOf(Cell)).apply(this, arguments));
  }

  _createClass(Cell, [{
    key: 'shouldComponentUpdate',
    value: function shouldComponentUpdate() {
      return false;
    }
  }, {
    key: 'render',
    value: function render() {
      return React.createElement(
        'span',
        { className: 'cell' },
        ''
      );
    }
  }]);

  return Cell;
}(React.Component);

;

var TileView = function (_React$Component3) {
  _inherits(TileView, _React$Component3);

  function TileView() {
    _classCallCheck(this, TileView);

    return _possibleConstructorReturn(this, (TileView.__proto__ || Object.getPrototypeOf(TileView)).apply(this, arguments));
  }

  _createClass(TileView, [{
    key: 'shouldComponentUpdate',
    value: function shouldComponentUpdate(nextProps) {
      if (this.props.tile != nextProps.tile) {
        return true;
      }
      if (!nextProps.tile.hasMoved() && !nextProps.tile.isNew()) {
        return false;
      }
      return true;
    }
  }, {
    key: 'render',
    value: function render() {
      var tile = this.props.tile;
      var classArray = ['tile'];
      if (this.props.tile.value < 0) {
        classArray.push('tilegift');
      } else {
        classArray.push('tile' + this.props.tile.value);
      }
      if (!tile.mergedInto) {
        classArray.push('position_' + tile.row + '_' + tile.column);
      }
      if (tile.mergedInto) {
        classArray.push('merged');
      }
      if (tile.isNew()) {
        classArray.push('new');
      }
      if (tile.hasMoved()) {
        classArray.push('row_from_' + tile.fromRow() + '_to_' + tile.toRow());
        classArray.push('column_from_' + tile.fromColumn() + '_to_' + tile.toColumn());
        classArray.push('isMoving');
      }
      var classes = classArray.join(' ');
      return React.createElement(
        'span',
        { className: classes },
        tile.value
      );
    }
  }]);

  return TileView;
}(React.Component);

var Score = function (_React$Component4) {
  _inherits(Score, _React$Component4);

  function Score() {
    _classCallCheck(this, Score);

    return _possibleConstructorReturn(this, (Score.__proto__ || Object.getPrototypeOf(Score)).apply(this, arguments));
  }

  _createClass(Score, [{
    key: 'render',
    value: function render() {
      var score = this.props.score;
      var addition = this.props.addition;
      // TODO: AnkiGame, The addition is not displayed if the previous move generated
      // non-zero addition
      var increment = addition > 0 ? React.createElement(
        'div',
        { className: 'score-addition' },
        '+',
        addition
      ) : React.createElement('span', null);
      return React.createElement(
        'div',
        { className: 'score-container' },
        React.createElement(
          'span',
          null,
          score
        ),
        increment
      );
    }
  }]);

  return Score;
}(React.Component);

var BestScore = function (_React$Component5) {
  _inherits(BestScore, _React$Component5);

  function BestScore() {
    _classCallCheck(this, BestScore);

    return _possibleConstructorReturn(this, (BestScore.__proto__ || Object.getPrototypeOf(BestScore)).apply(this, arguments));
  }

  _createClass(BestScore, [{
    key: 'render',
    value: function render() {
      var bestScore = this.props.bestScore;
      return React.createElement(
        'div',
        { className: 'best-container' },
        React.createElement(
          'span',
          null,
          bestScore
        )
      );
    }
  }]);

  return BestScore;
}(React.Component);

var Trick = function (_React$Component6) {
  _inherits(Trick, _React$Component6);

  function Trick() {
    _classCallCheck(this, Trick);

    return _possibleConstructorReturn(this, (Trick.__proto__ || Object.getPrototypeOf(Trick)).apply(this, arguments));
  }

  _createClass(Trick, [{
    key: 'render',
    value: function render() {
      var Board = this.props.Board;
      var points = Board.points;
      var coins = Board.coins;
      var tricks = Board.tricks;

      var trickName = this.props.trickName;

      var generateTrickClass = function generateTrickClass(trickName) {
        var trick = tricks[trickName];

        var requiredPoints = trick['points'];
        var availablePoints = points;

        var requiredCoins = trick['coins'];
        var availableCoins = coins;

        var isPermitted = trick['isPermitted'];

        var trickClass = 'trick';

        if (availablePoints >= requiredPoints) {
          trickClass += ' trick_' + trickName;
        } else {
          trickClass += ' trick_blocked';
        }

        if (availableCoins < requiredCoins || !isPermitted.apply(Board.state.board)) {
          trickClass += ' trick_disabled';
        }

        return trickClass;
      };

      var generateCoinsClass = function generateCoinsClass(trickName) {
        var trick = tricks[trickName];

        var requiredPoints = trick['points'];
        var availablePoints = points;

        var requiredCoins = trick['coins'];
        var availableCoins = coins;

        var coinsClass = 'trickCoins';

        if (availablePoints < requiredPoints) {
          coinsClass += ' trickCoinsBlocked';
        } else if (availableCoins < requiredCoins) {
          coinsClass += ' trickCoinsDisabled';
        }

        return coinsClass;
      };

      var generatePointsClass = function generatePointsClass(trickName) {
        var trick = tricks[trickName];

        var requiredPoints = trick['points'];
        var availablePoints = points;

        var pointsClass = 'trickPoints';

        if (availablePoints < requiredPoints) {
          pointsClass += ' trickPointsDisabled';
        }

        return pointsClass;
      };

      return React.createElement(
        'div',
        { className: 'trickContainer' },
        React.createElement(
          'div',
          null,
          React.createElement(
            'span',
            { className: generatePointsClass(trickName) },
            tricks[trickName]['points'],
            '\u2605'
          )
        ),
        React.createElement(
          'div',
          null,
          React.createElement(
            'span',
            { className: generateCoinsClass(trickName) },
            tricks[trickName]['coins'],
            '\u26C1'
          )
        ),
        React.createElement(
          'div',
          null,
          React.createElement('span', { className: generateTrickClass(trickName), onClick: Board.tryTrick.bind(Board, trickName) })
        )
      );
    }
  }]);

  return Trick;
}(React.Component);

var BoardViewRendered = ReactDOM.render(React.createElement(BoardView, null), document.getElementById('boardDiv'));

// To be called from Android
var restartGame = function restartGame(logCode) {
  if (typeof Anki !== "undefined") {
    Anki.getBoardState(BoardViewRendered.getBoardStateAsString(), logCode);
    BoardViewRendered.restartGame();
  }
};

var goToAnki = function goToAnki(logCode) {
  if (typeof Anki !== "undefined") {
    Anki.getBoardState(BoardViewRendered.getBoardStateAsString(), logCode);
  }
};

var continuePlaying = function continuePlaying() {
  BoardViewRendered.continuePlaying();
};