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
    console.log(history);
    this.state = {board: new Board(previousState, bestScore, history)};
  }
  restartGame() {
    // Do not restart the best score
    var bestScore = this.storageManager.getBestScore();
    this.setState({board: new Board(undefined, bestScore, undefined)});
    this.storageManager.clearGameState();
    this.storageManager.clearHistory();
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
  removeTwos(event) {
    event.preventDefault();
    
    //if(typeof(Anki) !== "undefined") {
      //if(Anki.hasMoneyForTrick("bomb", this.getBoardStateAsString())) {
        if(this.state.board.ableToDeleteTwos()){
          this.setState({board: this.state.board.removeTwos()});
        }
      //}
    //}
  }
  addGift(event) {
    event.preventDefault();

    //if(typeof(Anki) !== "undefined") {
        if(this.state.board.hasEmptyCells()) {
          this.setState({board: this.state.board.addGift()});
        }
    //}
  }
  undoLast(event) {
    event.preventDefault()

      //if(typeof(Anki) !== "undefined") {
        if(this.state.board.hasHistory()) {
          this.setState({board: this.state.board.undo()});
        }
    //}
  }
  double(event) {
    event.preventDefault()

      //if(typeof(Anki) !== "undefined") {
        if(this.state.board.ableToDouble()) {
          this.setState({board: this.state.board.double()});
        }
    //}
  }
  render() {
    // Since render is executed every time the state changes
    // Here we store the state of the game and the best score
    this.storageManager.setGameState(this.state.board.serialize());
    this.storageManager.setBestScore(this.state.board.bestScore);
    this.storageManager.setHistory(this.state.board.history);

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
          <span className='trick' onClick={this.removeTwos.bind(this)}>{''}</span>
          <span className='trick' onClick={this.addGift.bind(this)}>{''}</span>
          <span className='trick' onClick={this.undoLast.bind(this)}>{''}</span>
          <span className='trick' onClick={this.double.bind(this)}>{''}</span>
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

// Types of logs in Android
// NONE = 0;
// START_GAME = 1;
// END_GAME = 2;
// USE_TRICK = 3;
// SELECT_GAME_MODE = 4;
// CHECK_LEADERBOARD = 5;
// RESTART_GAME = 6;
// GO_TO_ANKI = 7;
// GO_TO_GAME = 8;
// TAKE_QUIZZ = 9;

// To be called from Android
var restartGame = function() {
  if(typeof(Anki) !== "undefined") {
    Anki.getBoardState(BoardViewRendered.getBoardStateAsString(), 6);
    BoardViewRendered.restartGame();
  }
}

var goToAnki = function () {
  if(typeof(Anki) !== "undefined") {
    Anki.getBoardState(BoardViewRendered.getBoardStateAsString(), 7)
  }
}