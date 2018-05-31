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
    this.storageManager = new LocalStorageManager;
    this.setup();
  }
  setup() {
    var previousState = this.storageManager.getGameState();
    var bestScore = this.storageManager.getBestScore();
    var history = this.storageManager.getHistory();
    this.state = {board: new Board(previousState, bestScore, history)};
    this.points = Anki.getAnkiPoints();
  }
  restartGame() {
    // Do not restart the best score
    var bestScore = this.storageManager.getBestScore();
    this.setState({board: new Board(undefined, bestScore, undefined)});
    this.storageManager.clearGameState();
    this.storageManager.clearHistory();
    this.points = Anki.getAnkiPoints();
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
  addGift(event) {
    event.preventDefault();

    var requiredCoins = 10;
    var trickName = "gift";
    var requiredPoints = 100;

    if(!Anki.hasPointsForTrick(requiredPoints)) {
      Anki.noPointsForTrick(trickName, requiredPoints, this.getBoardStateAsString());
      return;
    }
    
    if(!Anki.hasCoinsForTrick(requiredCoins)) {
      Anki.noCoinsForTrick(trickName, requiredCoins, this.getBoardStateAsString());
      return;
    }

    if(!this.state.board.hasEmptyCells()){
      Anki.unableTrick(trickName, this.getBoardStateAsString());
      return;
    }

    Anki.doTrick(trickName, requiredCoins, this.getBoardStateAsString());
    this.setState({board: this.state.board.addGift()});
  }
  double(event) {
    event.preventDefault();

    var requiredCoins = 20;
    var trickName = "double";
    var requiredPoints = 1000;
    
    if(!Anki.hasPointsForTrick(requiredPoints)) {
      Anki.noPointsForTrick(trickName, requiredPoints, this.getBoardStateAsString());
      return;
    }

    if(!Anki.hasCoinsForTrick(requiredCoins)) {
      Anki.noCoinsForTrick(trickName, requiredCoins, this.getBoardStateAsString());
      return;
    }

    if(!this.state.board.ableToDouble()){
      Anki.unableTrick(trickName, this.getBoardStateAsString());
      return;
    }

    Anki.doTrick(trickName, requiredCoins, this.getBoardStateAsString());
    this.setState({board: this.state.board.double()});
  }
  removeTwos(event) {
    event.preventDefault();

    var requiredCoins = 30;
    var trickName = "bomb";
    var requiredPoints = 5000;
    
    if(!Anki.hasPointsForTrick(requiredPoints)) {
      Anki.noPointsForTrick(trickName, requiredPoints, this.getBoardStateAsString());
      return;
    }
    
    if(!Anki.hasCoinsForTrick(requiredCoins)) {
      Anki.noCoinsForTrick(trickName, requiredCoins, this.getBoardStateAsString());
      return;
    }

    if(!this.state.board.ableToDeleteTwos()){
      Anki.unableTrick(trickName, this.getBoardStateAsString());
      return;
    }

    Anki.doTrick(trickName, requiredCoins, this.getBoardStateAsString());
    this.setState({board: this.state.board.removeTwos()});
  }
  undoLast(event) {
    event.preventDefault();

    var requiredCoins = 40;
    var trickName = "undo";
    var requiredPoints = 10000;
    
    if(!Anki.hasPointsForTrick(requiredPoints)) {
      Anki.noPointsForTrick(trickName, requiredPoints, this.getBoardStateAsString());
      return;
    }

    if(!Anki.hasCoinsForTrick(requiredCoins)) {
      Anki.noCoinsForTrick(trickName, requiredCoins, this.getBoardStateAsString());
      return;
    }

    if(!this.state.board.hasHistory()){
      Anki.unableTrick(trickName, this.getBoardStateAsString());
      return;
    }

    Anki.doTrick(trickName, requiredCoins, this.getBoardStateAsString());
    this.setState({board: this.state.board.undo()});
  }
  
  render() {
    // Since render is executed every time the state changes
    // Here we store the state of the game and the best score
    this.storageManager.setGameState(this.state.board.serialize());
    this.storageManager.setBestScore(this.state.board.bestScore);
    this.storageManager.setHistory(this.state.board.history);

    var points = this.points;

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
          <span className={'trick ' + (points >= 100 ? 'trickGift' : 'trickBlocked')} onClick={this.addGift.bind(this)}>{''}</span>
          <span className={'trick ' + (points >= 1000 ? 'trickDouble' : 'trickBlocked')} onClick={this.double.bind(this)}>{''}</span>
          <span className={'trick ' + (points >= 5000 ? 'trickBomb' : 'trickBlocked')} onClick={this.removeTwos.bind(this)}>{''}</span>
          <span className={'trick ' + (points >= 10000 ? 'trickUndo' : 'trickBlocked')} onClick={this.undoLast.bind(this)}>{''}</span>
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

