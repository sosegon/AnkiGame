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
//     return 10;
//   }
// }

class BoardView extends React.Component {
  constructor(props) {
    super(props);
    this.setup();
  }
  setup() {
    this.storageManager = new LocalStorageManager;
    var previousState = this.storageManager.getGameState();
    var bestScore = this.storageManager.getBestScore();
    var history = this.storageManager.getHistory();
    this.state = {board: new Board(previousState, bestScore, history)};
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
        points: 1000,
        isPermitted: this.state.board.ableToDouble,
        execute: this.state.board.double
      },
      bomb: {
        name: 'bomb',
        coins: 30,
        points: 5000,
        isPermitted: this.state.board.ableToDeleteTwos,
        execute: this.state.board.removeTwos
      },
      undo: {
        name: 'undo',
        coins: 40,
        points: 10000,
        isPermitted: this.state.board.hasHistory,
        execute: this.state.board.undo
      }
    };
  }
  restartGame() {
    // Do not restart the best score
    var bestScore = this.storageManager.getBestScore();
    this.setState({board: new Board(undefined, bestScore, undefined)});
    this.storageManager.clearGameState();
    this.storageManager.clearHistory();
    this.points = Anki.getAnkiPoints();
    this.coins = Anki.getAnkiCoins();
  }
  getBoardStateAsString() {
    return this.state.board.asString();
  }
  handleKeyDown(event) {
    if (event.keyCode >= 37 && event.keyCode <= 40) {
      event.preventDefault();
      var direction = event.keyCode - 37;
      this.setState({board: this.state.board.move(direction)});
    }
  }
  handleTouchStart(event) {
    if (event.touches.length != 1) {
      return;
    }
    this.startX = event.touches[0].screenX;
    this.startY = event.touches[0].screenY;
    event.preventDefault();
  }
  handleTouchEnd(event) {
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
      this.setState({board: this.state.board.move(direction)});
    }
  }
  componentDidMount() {
    window.addEventListener('keydown', this.handleKeyDown.bind(this));
  }
  componentWillUnmount() {
    window.removeEventListener('keydown', this.handleKeyDown.bind(this));
  }
  // This function has to be bound when assigning it to the child's
  // prop, otherwise, it won't work.
  tryTrick(trickName, event) {
    event.preventDefault();

    var trick = this.tricks[trickName];
    var requiredCoins = trick["coins"];
    var trickName = trick["name"];
    var requiredPoints = trick["points"];
    var isPermitted = trick["isPermitted"];
    var execute = trick["execute"];

    if(!Anki.hasPointsForTrick(requiredPoints)) {
      Anki.noPointsForTrick(trickName, requiredPoints, this.getBoardStateAsString());
      return;
    }
    
    if(!Anki.hasCoinsForTrick(requiredCoins)) {
      Anki.noCoinsForTrick(trickName, requiredCoins, this.getBoardStateAsString());
      return;
    }

    // Use apply to bind the object
    if(!isPermitted.apply(this.state.board)){
      Anki.unableTrick(trickName, this.getBoardStateAsString());
      return;
    }

    Anki.doTrick(trickName, requiredCoins, this.getBoardStateAsString());
    this.setState({board: execute.apply(this.state.board)});

    // After applying a trick, the number of coins changed. Update the variable
    // since the visual of the tricks depends on that value
    this.coins = Anki.getAnkiCoins();
  }
  render() {
    // Since render is executed every time the state changes
    // Here we store the state of the game and the best score
    this.storageManager.setGameState(this.state.board.serialize());
    this.storageManager.setBestScore(this.state.board.bestScore);
    this.storageManager.setHistory(this.state.board.history);

    var points = this.points;
    var coins = this.coins;

    var generateTrickClass = function(mainClass, values) {
      var trickClass = 'trick';
      var requiredPoints = values[0];
      var availablePoints = values[1];
      var requiredCoins = values[2];
      var availableCoins = values[3];

      if(availablePoints >= requiredPoints){
        trickClass += ' ' + mainClass;
        if(availableCoins < requiredCoins) {
          trickClass += ' trickDisabled';
        }
      } else {
        trickClass += ' trickBlocked'
      }

      return trickClass;
    }

    var bestScore = this.state.board.bestScore;
    var bestScoreElem = (
      <BestScore bestScore={bestScore} />
    );

    var currentScore = this.state.board.score;
    var addition = this.state.board.addition;
    var scoreElem = (
      <Score score={currentScore} addition={addition}/>
    );

    var cells = this.state.board.cells.map((row, rowIndex) => {
      return (
        <div key={rowIndex}>
          { row.map((_, columnIndex) => <Cell key={rowIndex * Board.size + columnIndex} />) }
        </div>
      );
    });
    var tiles = this.state.board.tiles
      .filter(tile => tile.value != 0)
      .map(tile => <TileView tile={tile} key={tile.id} />);
    return (
      <div>
        <div className="scores-container">
          {scoreElem} {bestScoreElem}
        </div>
        <div className='board' onTouchStart={this.handleTouchStart.bind(this)} onTouchEnd={this.handleTouchEnd.bind(this)} tabIndex="1">
          {cells}
          {tiles}
        </div>
        <div>
          <span className={generateTrickClass('trickGift', [100, points, 10, coins])} onClick={this.tryTrick.bind(this, 'gift')} />
          <span className={generateTrickClass('trickDouble', [1000, points, 20, coins])} onClick={this.tryTrick.bind(this, 'double')} />
          <span className={generateTrickClass('trickBomb', [5000, points, 30, coins])} onClick={this.tryTrick.bind(this, 'bomb')} />
          <span className={generateTrickClass('trickUndo', [10000, points, 40, coins])} onClick={this.tryTrick.bind(this, 'undo')} />
        </div>
        <div>
          <span className="trickPrice">10⛁</span>
          <span className="trickPrice">20⛁</span>
          <span className="trickPrice">30⛁</span>
          <span className="trickPrice">40⛁</span>
        </div>
        <div>
          <span className="trickPrice">100★</span>
          <span className="trickPrice">1000★</span>
          <span className="trickPrice">5000★</span>
          <span className="trickPrice">10000★</span>
        </div>
      </div>
    );
  }
};

class Cell extends React.Component {
  shouldComponentUpdate() {
    return false;
  }
  render() {
    return (
      <span className='cell'>{''}</span>
    );
  }
};

class TileView extends React.Component {
  shouldComponentUpdate(nextProps) {
    if (this.props.tile != nextProps.tile) {
      return true;
    }
    if (!nextProps.tile.hasMoved() && !nextProps.tile.isNew()) {
      return false;
    }
    return true;
  }
  render() {
    var tile = this.props.tile;
    var classArray = ['tile'];
    if(this.props.tile.value < 0) {
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
    return (
      <span className={classes}>{tile.value}</span>
    );
  }
}

class Score extends React.Component {
  render() {
    var score = this.props.score;
    var addition = this.props.addition;
    // TODO: AnkiGame, The addition is not displayed if the previous move generated
    // non-zero addition
    var increment = addition > 0 ? 
    (<div className="score-addition">+{addition}</div>) : 
    (<span/>);
    return (
      <div className="score-container">
        <span>{score}</span>
        {increment}
      </div>
    )
  }
}

class BestScore extends React.Component {
  render() {
    var bestScore = this.props.bestScore;
    return (
      <div className="best-container">
        <span>{bestScore}</span>
      </div>
    )
  }
}

var BoardViewRendered = ReactDOM.render(<BoardView />, document.getElementById('boardDiv'));

// To be called from Android
var restartGame = function(logCode) {
  if(typeof(Anki) !== "undefined") {
    Anki.getBoardState(BoardViewRendered.getBoardStateAsString(), logCode);
    BoardViewRendered.restartGame();
  }
}

var goToAnki = function (logCode) {
  if(typeof(Anki) !== "undefined") {
    Anki.getBoardState(BoardViewRendered.getBoardStateAsString(), logCode);
  }
}

