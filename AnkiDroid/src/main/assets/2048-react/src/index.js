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
    this.tricksRevealed = false;
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
      //this.tricksRevealed = false;
      this.setState({board: this.state.board.move(direction)});
      if(this.state.board.hasLost() && !this.ableToDoAnyTrick()) {
        Anki.hasLost();
      }
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
      //this.tricksRevealed = false;
      this.setState({board: this.state.board.move(direction)});
      if(this.state.board.hasLost() && !this.ableToDoAnyTrick()) {
        Anki.hasLost();
      }
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

    if(this.tricksRevealed === false) {
      return;
    }

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
    // TODO: AnkiGame, Executing the double trick can make end the game. Handle that
    Anki.doTrick(trickName, requiredCoins, this.getBoardStateAsString());
    this.setState({board: execute.apply(this.state.board)});

    // After applying a trick, the number of coins changed. Update the variable
    // since the visual of the tricks depends on that value
    this.coins = Anki.getAnkiCoins();
  }
  ableToDoAnyTrick() {
    var tricks = this.tricks;
    var board = this.state.board;
    var points = this.points;
    var coins = this.coins;

    var t = Object.keys(tricks).filter(function(key, index) {
      var trick = tricks[key];

      var requiredPoints = trick['points'];
      var availablePoints = points;
      if(availablePoints < requiredPoints) {
        return false;
      }

      var requiredCoins = trick['coins'];
      var availableCoins = coins;
      if(availableCoins < requiredCoins) {
        return false;
      }

      if(!trick['isPermitted'].apply(board)) {
        return false;
      }

      return true;
    });

    return t.length > 0;
  }
  continuePlaying() {
    this.state.board.continueAfterWon = true;
  }
  revealTricks() {
    this.tricksRevealed = !this.tricksRevealed;
    this.forceUpdate();
  }
  render() {
    // Since render is executed every time the state changes
    // Here we store the state of the game and the best score
    this.storageManager.setGameState(this.state.board.serialize());
    this.storageManager.setBestScore(this.state.board.bestScore);
    this.storageManager.setHistory(this.state.board.history);

    var tricks = this.tricks;
    var currentBoard = this;
    var trickElements = Object.keys(tricks).map(function(key, index){
      return (
        <Trick Board={currentBoard} trickName={key} />
      );
    });

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
        <div className="tricks-revealer" onClick={this.revealTricks.bind(this)}>
          {this.tricksRevealed ? '▼' : '▲'}
        </div>
        <div className={this.tricksRevealed ? 'tricks-curtain-drop' : 'tricks-curtain'}>
        </div>
        <div className="tricks-container">
          {trickElements}
        </div>
        <div className='board' onTouchStart={this.handleTouchStart.bind(this)} onTouchEnd={this.handleTouchEnd.bind(this)} tabIndex="1">
          {cells}
          {tiles}
        </div>
        <div className="scores-container">
          {scoreElem} {bestScoreElem}
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

class Trick extends React.Component {
  render() {
    var Board = this.props.Board;
    var points = Board.points;
    var coins = Board.coins;
    var tricks = Board.tricks;

    var trickName = this.props.trickName;

    var generateTrickClass = function(trickName) {
      var trick = tricks[trickName];

      var requiredPoints = trick['points'];
      var availablePoints = points;

      var requiredCoins = trick['coins'];
      var availableCoins = coins;

      var isPermitted = trick['isPermitted'];

      var trickClass = 'trick';

      if(availablePoints >= requiredPoints){
        trickClass += ' trick_' + trickName;
      } else {
        trickClass += ' trick_blocked';
      }

      if(availableCoins < requiredCoins || !isPermitted.apply(Board.state.board)) {
        trickClass += ' trick_disabled';
      }

      return trickClass;
    }

    var generateCoinsClass = function(trickName) {
      var trick = tricks[trickName];

      var requiredPoints = trick['points'];
      var availablePoints = points;

      var requiredCoins = trick['coins'];
      var availableCoins = coins;

      var coinsClass = 'trickCoins';

      if(availablePoints < requiredPoints){
        coinsClass += ' trickCoinsBlocked';
      } else if(availableCoins < requiredCoins) {
        coinsClass += ' trickCoinsDisabled';
      }

      return coinsClass;
    }

    var generatePointsClass = function(trickName) {
      var trick = tricks[trickName];

      var requiredPoints = trick['points'];
      var availablePoints = points;

      var pointsClass = 'trickPoints';

      if(availablePoints < requiredPoints) {
        pointsClass += ' trickPointsDisabled';
      }

      return pointsClass;
    }

    return (
      <div className="trickContainer">
        <div>
          <span className={generateTrickClass(trickName)} onClick={Board.tryTrick.bind(Board, trickName)}/>
        </div>
        <div>
          <span className={generatePointsClass(trickName)}>{tricks[trickName]['points']}☆</span>
        </div>
        <div>
          <span className={generateCoinsClass(trickName)}>{tricks[trickName]['coins']}⛁</span>
        </div>
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

var continuePlaying = function() {
  BoardViewRendered.continuePlaying();
};

// Adjust the board to the device's screen
var rescale = function(screenHeight, screenWidth) {
  //var screenHeight = screen.height;
  //var screenWidth = screen.width;
  var screenHeight = screenHeight;// | window.innerHeight; //* window.devicePixelRatio;
  var screenWidth = screenWidth;// | window.innerWidth; //* window.devicePixelRatio;
  var screenRatio = screenWidth / screenHeight;

  // Scrolling on the following devices
  // PIXEL 2XL landscape
  // iPhone 5/SE landscape
  // iPhone 6/7/8 landscape
  // iPhone 6/7/8 plus landscape
  // iPhone X landscape
  // iPad landscape and portrait
  // iPad Pro landscape and portrait
  var antiScroll = 1.2;
  var gameWidth = 450 * antiScroll;
  var gameHeight = 733 * antiScroll;
  var gameRatio = gameWidth / gameHeight;

  //console.log("screen ratio: " + screenRatio + ", game ratio: " + gameRatio);
  var scale = 1;

  // gameHeight > gameWidth always
  // The game has to fit the screen device always
  //console.log("screenHeight: " + screenHeight);
  //console.log("screenWidth:  " + screenWidth);
  //console.log("gameHeight:   " + gameHeight);
  //console.log("gameWidth:    " + gameWidth);
  if(screenHeight >= screenWidth) {
    if(screenHeight >= gameHeight && screenWidth >= gameWidth) { // Consider ratios
      if(screenRatio >= gameRatio) { // Increase gameHeight
        scale = screenHeight / gameHeight;
        //console.log("case 1_1");
      } else { // Increase gameWidth
        scale = screenWidth / gameWidth;
        //console.log("case 1_2"); // never happens
      }
    } else if(screenHeight >= gameHeight && screenWidth < gameWidth){ // Decrease the gameWidth
      scale = screenWidth / gameWidth;
      //console.log("case 2");
    } else if(screenHeight < gameHeight && screenWidth >= gameWidth){ // Decrease the gameHeight
      scale = screenHeight / gameHeight;
      //console.log("case 3");
    } else { // Consider ratios
      if(screenRatio >= gameRatio) { // Decrease gameHeight
        scale = screenHeight / gameHeight;
        //console.log("case 4_1");
      } else { // Decrease gameWidth
        scale = screenWidth / gameWidth;
        //console.log("case 4_2");
      }
    }
  } else {
    if(screenHeight >= gameHeight && screenWidth >= gameWidth) { // Consider ratios
      if(screenRatio >= gameRatio) { // Increase gameHeight
        scale = screenHeight / gameHeight;
        //console.log("case 5_1");
      } else { // Increase gameWidth
        scale = screenWidth / gameWidth;
        //console.log("case 5_2"); // never happens
      }
    } else if(screenHeight >= gameHeight && screenWidth < gameWidth){ // Decrease the gameWidth
      scale = screenWidth / gameWidth;
      //console.log("case 6"); // never happens
    } else if(screenHeight < gameHeight && screenWidth >= gameWidth){ // Decrease the gameHeight
      scale = screenHeight / gameHeight;
      //console.log("case 7");
    } else { // Consider ratios
      if(screenRatio >= gameRatio) { // Decrease gameHeight
        scale = screenHeight / gameHeight;
        //console.log("case 8_1");
      } else { // Decrease gameWidth
        scale = screenWidth / gameWidth;
        //console.log("case 8_2"); // never happens
      }
    }
  }

  var board = document.getElementById("boardDiv");
  board.style.zoom = scale;
  board.style.display = "block";
  //console.log("scalew: " + scale);
};

//window.setTimeout(rescale, 300);