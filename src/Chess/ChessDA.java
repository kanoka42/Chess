package Chess;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

import Chess.chess.RandomNumbers;

public class ChessDA{
	/* This enum represents values for each of the possible "pieces" that can
	 * occupy a space on the chess board, including "blank" for a vacant space.
	 */
	
	
	public static final short BLACK = 0;
	
	public static final short WHITE = 1;
	
	public static final short CLEAR = 2;
	
	public static final short CHECK_MOVE = 1; // this is a piece move "mode" designating an AI board evaluation
	
	public static final short REAL_MOVE = 2; // this is a piece move "mode" designating an actual AI move

	public enum Type
	{
	  BLANK,
	  PAWN,
	  BISHOP,
	  KNIGHT,
	  ROOK,
	  QUEEN,
	  KING;

	    public int getValue()
	    {
	        return this.ordinal();
	    }

	    public static Type forValue(int value)
	    {
	        return values()[value];
	    }
	}

	public class Coord
	{
	  public short m_X;
	  public short m_Y;
	}

	public class GlobalMembers
	{
	    /* These const values are used for identifying whether the "piece" occupying
	     * a chess board space is black, white, or in the case of a blank spot,
	     * "clear."
	     */

	    public static final short BLACK = 0;

	    public static final short WHITE = 1;

	    public static final short CLEAR = 2;

	    public static final short CHECK_MOVE = 1; // this is a piece move "mode" designating an AI board evaluation

	    public static final short REAL_MOVE = 2; // this is a piece move "mode" designating an actual AI move
	}


	public class ChessPiece
	{

	  /* The ctor for ChessPiece takes a piece type and color.
	   */

	  public ChessPiece(Type rOOK, short color)
	  {
	      this.m_type = rOOK;
	      this.m_color = color;
	      this.m_moves = 0;
	  }
	  public ChessPiece(ChessPiece chessPiece) {
		// TODO Auto-generated constructor stub
	}
	public ChessPiece(int pAWN, short black) {
		// TODO Auto-generated constructor stub
	}
	public void dispose()
	  {
	  }

	  /* This member function returns the chess piece type.
	   */

	  public final Type getType()
	  {
	    return m_type;
	  }

	  /* This member function returns the chess piece color.
	   */

	  public final short getColor()
	  {
	    return m_color;
	  }

	  /* This function is used to record a new movement of a piece. Movement tracking
	   * is necessary for evaluating validity of special piece moves like en passant
	   * capture and castling.
	   */

	  public final void incrementMoves()
	  {
	    ++m_moves;
	  }

	  /* Reduces the number of times a piece has been moved. Used for adjustment when
	   * a function incidentally increments the movement of a piece in a case where
	   * a board evaluation is taking place, and is not intended as an actual move.
	   */

	  public final void decrementMoves()
	  {
	    --m_moves;
	  }

	  /* Returns the number of times a piece has been moved.
	   */

	  public final int getMoves()
	  {
	    return m_moves;
	  }
	  private Type m_type = Type.BLANK; // this variable holds the type of piece this object represents

	  private short m_color; // this variable holds the color of the piece, white or black
	  private int m_moves; // this variable holds the number of moves the piece has made
	}



	/* This is the class to handle the layout of the chess board.
	 */

	public class ChessBoard
	{
	
	
	  private final Type BLANK = null;
	  private final int PAWN = (Integer) null;
	  private final int KING = (Integer) null;
      private final int QUEEN = (Integer) null;
	  private final int BISHOP = (Integer) null;
	  private final int ROOK = (Integer) null;
	  private final int KNIGHT = (Integer) null;
	  private ChessPiece[][] m_locations1; // this will be an array to represent a chess board grid
	  public TreeMap<Type, Short>[] m_pieceMaps[][]; // this map tracks number of pieces left in a player's arsenal
	  private Pair<Short, Short>[] m_lastMoves = new Pair[2]; // this pair is used in determining if there is a repetition of moves
	  private short[] m_lastMoveRepeats = new short[2]; // this counts the number of times the same moves have been repeated
	  private short m_lastMoveX; // this is the last move made, used for checking if en Passant is valid
	  private short m_lastMoveY; // this is the 'Y' coordinate of the last move (see above)
	  private int m_capturelessMoves; // this variable counts moves where no pawns are moved or captures made
	  
	  
	  
	  public ChessBoard()
		{
			this.m_capturelessMoves = 0;
		  m_lastMoves[BLACK].first = 8; // beginning "last move" of the black piece as "nothing"
		  m_lastMoves[BLACK].second = 8; // ^-- in this case, not a valid move at all
		  m_lastMoves[WHITE].first = 8; // similarly, we set the white pieces' last move.
		  m_lastMoves[WHITE].second = 8;
		  m_lastMoveRepeats[BLACK] = 0; // here we note that there has been no repetition of moves yet
		  m_lastMoveRepeats[WHITE] = 0; // ^-- for either black or white pieces.
		  m_locations1 = new ChessPiece[8][];
		  for (int i = 0; i < 8; ++i) // this loop creates a chess board grid for holding piece objects
		  {
			m_locations1[i] = new ChessPiece[8];
		  }
		  ChessPiece piece;
		  piece = new ChessPiece(ROOK, BLACK); // following lines set up some pieces on the board for black
		  m_locations1[0][0] = piece;
		  piece = new ChessPiece(ROOK, BLACK);
		  m_locations1[7][0] = piece;
		  piece = new ChessPiece(KNIGHT, BLACK);
		  m_locations1[1][0] = piece;
		  piece = new ChessPiece(KNIGHT, BLACK);
		  m_locations1[6][0] = piece;
		  piece = new ChessPiece(BISHOP, BLACK);
		  m_locations1[2][0] = piece;
		  piece = new ChessPiece(BISHOP, BLACK);
		  m_locations1[5][0] = piece;
		  piece = new ChessPiece(QUEEN, BLACK);
		  m_locations1[3][0] = piece;
		  piece = new ChessPiece(KING, BLACK);
		  m_locations1[4][0] = piece;
		  for (int i = 0; i < 8; ++i) // this loop creates black pawn pieces to setup the board
		  {
			piece = new ChessPiece(PAWN, BLACK);
			m_locations1[i][1] = piece;
		  }
		  piece = new ChessPiece(ROOK, WHITE); // setting up the white king's row here
		  m_locations1[0][7] = piece;
		  piece = new ChessPiece(ROOK, WHITE);
		  m_locations1[7][7] = piece;
		  piece = new ChessPiece(KNIGHT, WHITE);
		  m_locations1[1][7] = piece;
		  piece = new ChessPiece(KNIGHT, WHITE);
		  m_locations1[6][7] = piece;
		  piece = new ChessPiece(BISHOP, WHITE);
		  m_locations1[2][7] = piece;
		  piece = new ChessPiece(BISHOP, WHITE);
		  m_locations1[5][7] = piece;
		  piece = new ChessPiece(QUEEN, WHITE);
		  m_locations1[3][7] = piece;
		  piece = new ChessPiece(KING, WHITE);
		  m_locations1[4][7] = piece;
		  for (int i = 0; i < 8; ++i) // and now we place white pawns
		  {
			piece = new ChessPiece(PAWN, WHITE);
			m_locations1[i][6] = piece;
		  }
		  for (int i = 0; i < 8; ++i) // the rest of the board is represented by "blank" pieces of "clear" color
		  {
			for (int j = 2; j < 6; ++j)
			{
			  piece = new ChessPiece(BLANK, CLEAR);
			  m_locations1[i][j] = piece;
			}
		  }
		  for (short i = 0; i < 2; ++i) // we set up our piece map that records which pieces a player has left
		  {
			m_pieceMaps[i][PAWN] = 8;
			m_pieceMaps[i][ROOK] = 2;
			m_pieceMaps[i][KNIGHT] = 2;
			m_pieceMaps[i][BISHOP] = 2;
			m_pieceMaps[i][QUEEN] = 1;
			m_pieceMaps[i][KING] = 1;
		  }
		} 
	  
	  
	  public void copy1(ChessBoard src)
		{
		  for (int i = 0; i < 8; ++i)
		  {
			for (int j = 0; j < 8; ++j)
			{
			  m_locations1[i][j] = new ChessPiece(src.m_locations1[i][j]);
			}
		  }
		}
	  
	  
	  
	  private ChessBoard ChessBoard(ChessBoard rhs)
	  {
	    if (this == rhs)
	    {
	  	return this;
	    }
	    copy1(rhs);
	    return this;
	  }


		public ChessBoard(ChessBoard src)
	  	{
	  	  m_locations1 = new ChessPiece[8][];
	  	  for (int i = 0; i < 8; ++i)
	  	  {
	  		m_locations1[i] = new ChessPiece[8];
	  	  }
	  	  copy1(src);
	  	}
	  	public void dispose()
	  	{
	  	  for (int i = 0; i < 8; ++i)
	  	  {
	  		for (int j = 0; j < 8; ++j)
	  		{
	  		  m_locations1[i][j] = null;
	  		}
	  		m_locations1[i] = null;
	  	  }
	  	  m_locations1 = null;
	  	}
	  	
	  	
	  	
	  	
	  	public void display()
		{
		  int c;
		  byte cc;
		  boolean rightWhite;
		  System.out.print('\n');
		  System.out.print(32);
		  for (int i = 0; i < 8; ++i)
		  {
			System.out.print(32);
			System.out.print(32);
			System.out.print(97 + i);
			System.out.print(32);
		  }
		  System.out.print('\n');
		  System.out.print(32);
		  System.out.print(218);
		  for (int i = 0; i < 8; ++i)
		  {
			System.out.print(196);
			System.out.print(196);
			System.out.print(196);
			if (i < 7)
			{
			  System.out.print(194);
			}
			else
			{
			  System.out.print(191);
			}
		  }
		  System.out.print('\n');
		  cc = (byte)'8';
		  rightWhite = false;
		  for (int i = 0; i < 8; ++i)
		  {
			System.out.print(cc--);
			System.out.print(179);
			for (int j = 0; j < 8; ++j)
			{
			  if (rightWhite)
				  if ( (((j + i % 2) % 2) == 8))
				  	{
					  System.out.print(32);
				  	}
			  else
			  {
			  if (!rightWhite && ((j + i % 2) % 2) == 8)
			  {
				System.out.print(32);
			  }
			  else
			  {
			  if (m_locations1[j][i].getType() != BLANK)
			  {
				System.out.print(221);
			  }
			  else
			  {
				System.out.print(219);
			  }
			  }
			  }
			  switch (m_locations1[j][i].getType())
			  {
				case PAWN:
				  c = (byte)'p';
				  break;
				case ROOK:
				  c = (byte)'r';
				  break;
				case KNIGHT:
				  c = (byte)'n';
				  break;
				case BISHOP:
				  c = (byte)'b';
				  break;
				case QUEEN:
				  c = (byte)'q';
				  break;
				case KING:
				  c = (byte)'k';
				  break;
				default:
				  if (rightWhite && ((j + i % 2) % 2) == 8)
				  {
					c = (byte)32;
				  }
				  else
				  {
				  if (!rightWhite && ((j + i % 2) % 2) == 8)
				  {
					c = (byte)32;
				  }
				  else
				  {
					c = (byte)219;
				  }
				  }
			 
			  if (c != (byte)32 && c != (byte)219) // we're drawing the pieces, and this if statement will generate
			  {
				c = c - (32 * m_locations1[j][i].getColor()); // capital letters for the white ones
			  }
			  System.out.print(c);
			}
			if (rightWhite && ((j + i % 2) % 2) == 8)
			{
					System.out.print(32);
			}
				  else
				  {
				  if (!rightWhite && ((j + i % 2) % 2) == 8)
				  {
					System.out.print(32);
				  }
				  else
				  {
				  if (m_locations1[j][i].getType() != BLANK)
				  {
					System.out.print(222);
				  }
				  else
				  {
					System.out.print(219);
				  }
				  }
				  }
				  System.out.print(179);
				  rightWhite = !rightWhite;
				}
				System.out.print('\n');
				System.out.print(32);
				if (i < 7)
				{
				  System.out.print(195);
				  for (int j = 0; j < 8; ++j)
				  {
					System.out.print(196);
					System.out.print(196);
					System.out.print(196);
					if (j < 7)
					{
					  System.out.print(197);
					}
					else
					{
					  System.out.print(180);
					}
				  }
				}
				else
				{
				  System.out.print(192);
				  for (int j = 0; j < 8; ++j)
				  {
					System.out.print(196);
					System.out.print(196);
					System.out.print(196);
					if (j < 7)
					{
					  System.out.print(193);
					}
					else
					{
					  System.out.print(217);
					}
				  }
				}
				System.out.print('\n');
			  }
		}
		
	  	
	  	public void flip()
		{
		  ChessPiece piece;
		  for (int i = 0; i < 4; ++i)
		  {
			for (int j = 0; j < 8; ++j)
			{
			  piece = m_locations1[j][i];
			  m_locations1[j][i] = m_locations1[7 - j][7 - i];
			  m_locations1[7 - j][7 - i] = piece;
			}
		  }
		}
	  	
	  	public ChessPiece at(int i2, int i)
		{
		  return m_locations1[i2][i];
		}
	  	
	  	
	  	
		 /* This function checks to see if the last move made was to X,Y passed in,
		  * from the perspective of a flipped board, it is used for AI processing.
		  */
	  	
	  	
	  	 boolean ChessBoard1; boolean lastMoveWas(short X, short Y)
		 {
		   return (7 - m_lastMoveX == X && 7 - m_lastMoveY == Y);
		 }

		 /* This function replaces a piece object on the board with a new one supplied
		  */

		 void ChessBoardreplace(ChessPiece  piece, short X, short Y)
		 {
		   m_locations1[X][Y] = null;
		   m_locations1[X][Y] = piece;
		 }

		 
		 public boolean isPotentialDraw(short turn)
			{
			  return m_capturelessMoves >= 50 || (m_lastMoveRepeats[turn] > 2 && m_lastMoveRepeats[(turn + 1) % 2] > 3);
			}
		 
		 
			public boolean callDraw(short turn)
			{
			  if (isPotentialDraw(turn))
			  {
				System.out.print((turn == BLACK ? "Black " : "White "));
				if (m_capturelessMoves >= 50)
				{
				  System.out.print("calls draw based on 50 move rule: No captures made or " + "pawns moved.");
				  System.out.print("\n");
				  return true;
				}
				System.out.print("calls draw based on threefold repetition of moves.");
				System.out.print("\n");
				return true;
			  }
			  return false;
			}
			
			public boolean enPassantOption(short color)
			{
			  m_lastMoveRepeats[(color + 1) % 2] = 1;
			  return true;
			}
			
			
			public void copy(ChessBoard src)
			{
			  for (int i = 0; i < 8; ++i)
			  {
				for (int j = 0; j < 8; ++j)
				{
				  m_locations1[i][j] = new ChessPiece(src.m_locations1[i][j]);
				}
			  }
			}

			
			
			//void play();
			void move(int i, int j, int k, int l) {
			}
			//boolean goodMove(ChessBoard NamelessParameter1, String NamelessParameter2, ushort NamelessParameter3);
			//boolean inRange(ChessBoard NamelessParameter1, ChessPiece NamelessParameter2, ushort NamelessParameter3, ushort NamelessParameter4, ushort NamelessParameter5, ushort NamelessParameter6);
			//boolean pathClear(ChessBoard NamelessParameter1, ChessPiece NamelessParameter2, ushort NamelessParameter3, ushort NamelessParameter4, ushort NamelessParameter5, ushort NamelessParameter6);
			//boolean putInCheck(ChessBoard NamelessParameter1, ushort NamelessParameter2, ushort NamelessParameter3, ushort NamelessParameter4, ushort NamelessParameter5, ushort NamelessParameter6);
			//boolean putInCheck(ChessBoard NamelessParameter1, ushort NamelessParameter2);
			//boolean inJeopardy(ChessBoard NamelessParameter1, ushort NamelessParameter2, ushort NamelessParameter3, ushort NamelessParameter4, ushort NamelessParameter5, ushort NamelessParameter6);
			//boolean isStalemate(ChessBoard NamelessParameter1, ushort NamelessParameter2);
			//boolean isCheckMate(ChessBoard NamelessParameter1, ushort NamelessParameter2);
			//void getKingLocation(ChessBoard NamelessParameter1, ushort NamelessParameter2, ushort[] NamelessParameter3);
			//boolean isDraw(ChessBoard NamelessParameter);
			//boolean sameSquareColorBishops(ChessBoard NamelessParameter);
			//void computerMove(ChessBoard NamelessParameter1, ushort NamelessParameter2);
			//String allCaps(String NamelessParameter);
			//void displayHelp();
			
			
			private int Main()
			{
			  RandomNumbers.seed((int)(Time(0)));
			  play();
			  return 1;
			}

			/* This is the game play loop, it governs the the player's turns or delegates
			 * a move to the computer to play.
			 */



	  
			
			private void play()
			{
			  boolean gameOver = false; // game-play sentinel boolean variable
			  boolean computerPlay; // will the computer be playing? this bool hold the answer.
			  boolean skipTurn = false; // do we need to skip a color's turn? it depends on who moves first
			  String move; // this string is used for input of move coordinates as well as game options
			  short turn; // this holds the "color" of whose turn it is
			  ChessBoard board = new ChessBoard(); // we create the ChessBoard object
			  turn = WHITE;
			  System.out.print("\nWelcome to the game of Chess!\n");
			  System.out.print("\n");
			  do
			  { // We need to get some info about how the game will be played, first.
				System.out.print("Enter 1 for computer play, 2 for human vs. human: ");
				move = new Scanner(System.in).next();
			  } while ((move.charAt(0) != '1' || move.charAt(0) != '2'));
			  if (move.charAt(0) == '1')
			  {
				computerPlay = true;
				do
				{
				  System.out.print("Enter W to play white pieces, or B to play black: ");
				  move = new Scanner(System.in).nextLine();
				  move.toUpperCase();
				} while ((move.charAt(0))!=('W') && (move.charAt(0)) != ('B'));
				if ((move.charAt(0)) == ('B'))
				{
				  skipTurn = true;
				  turn = BLACK;
				  board.flip();
				}
				else
				{
				  skipTurn = false;
				}
			  }
			  else
			  {
				computerPlay = false;
			  }
			  System.out.print((computerPlay ? "\nComputer competitor mode." : "\nHuman vs. " + "human mode."));
			  System.out.print("\n");
			  if (computerPlay)
			  {
				System.out.print((skipTurn ? "Playing black pieces." : "Playing white pieces."));
				System.out.print("\n");
			  }
			  System.out.print("Enter ? for help.");
			  System.out.print("\n");
			  do
			  { // here we enter the game-play loop
				if (!skipTurn)
				  do
			  {
					board.display(); // show the board
					if (putInCheck(board, turn)) // the loop checks the board configuration for certain scenarios
					{
					  if (isCheckMate(board, turn))
					  {
						System.out.print("Check Mate!");
						System.out.print("\n");
						gameOver = true;
						break;
					  }
					  System.out.print("You are in check.");
					  System.out.print("\n");
					}
					else
					{
					if (isStalemate(board, turn))
					{
					  System.out.print("Stalemate!");
					  System.out.print("\n");
					  gameOver = true;
					  break;
					}
					else
					{
					if (isDraw(board))
					{
					  System.out.print("Draw!");
					  System.out.print("\n");
					  gameOver = true;
					  break;
					}
					}
					if (board.isPotentialDraw(turn))
					{
					  System.out.print("Type \"DRAW\" to declare Draw. ");
					}
					System.out.print(':');
					move = new Scanner(System.in).nextLine();
					move.toUpperCase();
					if ((move.charAt(0)) == ('Q'))
					{
					  gameOver = true;
					  break;
					}
					if (board.isPotentialDraw(turn) && allCaps(move).equals("DRAW"))
					{
					  gameOver = board.callDraw(turn);
					  if (gameOver)
					  {
						System.out.print("Draw!");
						System.out.print("\n");
					  }
					  break;
					}
					if (move.equals("?"))
					{
					  displayHelp();
					}
					while (!goodMove(board, move, turn));
					}
			  }while (!goodMove(board, move, turn));
				else
				{
				  skipTurn = false;
				}
				board.flip();
				if (turn == WHITE)
				{
				  turn = BLACK;
				}
				else
				{
				  turn = WHITE;
				}
				if (computerPlay && !gameOver) // if the computer is playing, it will make its move here
				{
				  if (isCheckMate(board, turn))
				  {
					System.out.print("Check Mate!");
					System.out.print("\n");
					gameOver = true;
					break;
				  }
				  else
				  {
				  if (isStalemate(board, turn))
				  {
					System.out.print("Stalemate!");
					System.out.print("\n");
					gameOver = true;
					break;
				  }
				  else
				  {
				  if (isDraw(board))
				  {
					System.out.print("Draw!");
					System.out.print("\n");
					gameOver = true;
					break;
				  }
				  else
				  {
				  if (board.isPotentialDraw(turn))
				  {
					gameOver = board.callDraw(turn);
					if (gameOver)
					{
					  System.out.print("Draw!");
					  System.out.print("\n");
					}
					break;
				  }
				  }
				  }
				  }
				  computerMove(board, turn);
				  if (turn == WHITE)
				  {
					turn = BLACK;
				  }
				  else
				  {
					turn = WHITE;
				  }
				  board.flip();
				}
			  } while (!gameOver);
			}


				private boolean goodMove(ChessBoard board, String move, short turn)
				{
				  if (move.equals("?"))
				  {
					return false;
				  }
				  if (move.length() != 5)
				  {
					System.out.print("\nErroneous input.");
					System.out.print("\n");
					return false;
				  }
				  byte beginCoordX;
				  byte beginCoordY;
				  byte endCoordX;
				  byte endCoordY;
				  beginCoordX = (byte) move.charAt(0); // we're converting the text input into numerical coordinates
				  beginCoordY = (byte) move.charAt(1);
				  endCoordX = (byte) move.charAt(3);
				  endCoordY = (byte) move.charAt(4);
				  beginCoordX = (byte) Character.toUpperCase(beginCoordX);
				  endCoordX = (byte) Character.toUpperCase(endCoordX);
				  beginCoordX -= 65;
				  endCoordX -= 65;
				  beginCoordY -= 49;
				  endCoordY -= 49;
				  beginCoordY = (byte) (7 - beginCoordY);
				  endCoordY = (byte) (7 - endCoordY);
				  if (!((beginCoordX >= 0 && beginCoordX < 8) && (beginCoordY >= 0 && beginCoordY < 8) && (endCoordX >= 0 && endCoordX < 8) && (endCoordY >= 0 && endCoordY < 8))) // we respond to some potential errors in input for this turn
				  {
					System.out.print("\nErroneous input.");
					System.out.print("\n");
					return false;
				  }
				  ChessPiece piece;
				  piece = new ChessPiece(board.at(beginCoordX, beginCoordY));
				  if (piece.getColor() != turn || piece.getType() == BLANK)
				  {
					System.out.print("\nYou need to move one of your pieces.\nInvalid move.");
					System.out.print("\n");
					piece = null;
					return false;
				  }
				  if (beginCoordX == endCoordX && beginCoordY == endCoordY)
				  {
					System.out.print("\nInvalid move.");
					System.out.print("\n");
					piece = null;
					return false;
				  }
				  if (inRange(board, piece, beginCoordX, beginCoordY, endCoordX, endCoordY) && pathClear(board, piece, beginCoordX, beginCoordY, endCoordX, endCoordY)) // we're checking to see if a move is possible given the coordinates
				  {
					if (piece.getType() == Type.KING && putInCheck(board, piece.getColor()) && beginCoordY == endCoordY && (beginCoordX - 2 == endCoordX || beginCoordX + 2 == endCoordX)) // handling an attempt to castle out of check here
					{
					  System.out.print("\nYou may not castle out of check.");
					}
					else
					{
					if (piece.getType() == Type.KING && beginCoordY == endCoordY && (beginCoordX - 2 == endCoordX || beginCoordX + 2 == endCoordX) && ((beginCoordX - 2 == endCoordX && putInCheck(board, piece.getColor(), beginCoordX, beginCoordY, beginCoordX - 1, endCoordY)) || (beginCoordX + 2 == endCoordX && putInCheck(board, piece.getColor(), beginCoordX, beginCoordY, beginCoordX + 1, endCoordY)))) // handling an attempt to castle THROUGH check, here
					{
					  System.out.print("\nYou may not castle through check.");
					}
					else
					{
					if (!putInCheck(board, piece.getColor(), beginCoordX, beginCoordY, endCoordX, endCoordY)) // handling a successful move
					{
					  board.move(beginCoordX, beginCoordY, endCoordX, endCoordY);
					  if (piece.getType() == Type.KING && beginCoordY == endCoordY && (beginCoordX - 2 == endCoordX || beginCoordX + 2 == endCoordX)) // ...and handling a castle move, if that's the case
					  {
						System.out.print("\nCastle move.");
						System.out.print("\n");
						if (beginCoordX > endCoordX)
						{
						  board.move(0, 7, 2 + piece.getColor(), 7);
						}
						else
						{
						  board.move(7, 7, 4 + piece.getColor(), 7);
						}
					  }
					  piece = null;
					  return true;
					}
					else
					{
					  System.out.print("\nThat move would leave you in check.");
					}
					}
					}
				  }
				  System.out.print("\nInvalid move.");
				  System.out.print("\n");
				  piece = null;
				  return false;
				}
				
				
				private boolean inRange(ChessBoard board, ChessPiece piece, int i, int j, int k, int l)
				{
					ChessPiece target;
					target = board.at(k, l);
					switch (piece.getType())
					{
					case PAWN:
						if ((j - l == 1 && i == k) || (piece.getMoves() == 0 && j - l == 2 && i == k))
						{
							return true;
						}
						if (j - l == 1 && ((k - 1 == i || k + 1 == i) && target.getType() != BLANK && target.getColor() != piece.getColor()))
						{
							return true;
						}
						if (j - l == 1 && k != i && board.at(k, l + 1).getType() == Type.PAWN && board.at(k, l + 1).getMoves() == 1 && l == 2 && board.lastMoveWas((short) k, (short) (l + 1)))
						{
							return board.enPassantOption(piece.getColor());
						}
						return false;
					case ROOK:
						if ((i == k && j != l) || (i != k && j == l))
						{
							return true;
						}
						return false;
					case KNIGHT:
						if ((i == k - 2 || i == k + 2) && (j == l - 1 || j == l + 1))
						{
							return true;
						}
						if ((j == l - 2 || j == l + 2) && (i == k - 1 || i == k + 1))
						{
							return true;
						}
						return false;
					case BISHOP:
						if (i > k && (j - l == i - k || l - j == i - k))
						{
							return true;
						}
						if (i < k && (j - l == k - i || l - j == k - i))
						{
							return true;
						}
						return false;
					case QUEEN:
						if ((i == k && j != l) || (i != k && j == l))
						{
							return true;
						}
						if (i > k && (j - l == i - k || l - j == i - k))
						{
							return true;
						}
						if (i < k && (j - l == k - i || l - j == k - i))
						{
							return true;
						}
						return false;
					case KING:
						if (i <= k + 1 && i >= k - 1 && j <= l + 1 && j >= l - 1)
						{
							return true;
						}
						if (j == l && ((i - 2 == k && board.at((short) 0, l).getMoves() == 0) || (i + 2 == k && board.at((short) 7, l).getMoves() == 0)) && piece.getMoves() == 0 && l == 7)
						{
							return true;
						}
						return false;
					}
					return false;
				}
				
				private boolean pathClear(ChessBoard board, ChessPiece piece, int i2, int j2, int k, int l)
				{
					switch (piece.getType())
					{
					case PAWN:
						if (i2 == k && board.at(k, l).getType() != BLANK)
						{
							return false;
						}
						if (i2 == k && j2 - 2 == l && board.at(k, l + 1).getType() != BLANK)
						{
							return false;
						}
						return true;
					case ROOK:
						if (i2 == k)
						{
							if (j2 < l)
							{
								for (int i = j2 + 1; i <= l; ++i)
								{
									if ((board.at(k, i).getType() != BLANK && i != l) || board.at(k, i).getColor() == piece.getColor())
									{
										return false;
									}
								}
							}
							else
							{
								for (int i = j2 - 1; i >= l; --i)
								{
									if ((board.at(k, i).getType() != BLANK && i != l) || board.at(k, i).getColor() == piece.getColor())
									{
										return false;
									}
								}
							}
						}
						else
						{
							if (i2 < k)
							{
								for (int i = i2 + 1; i <= k; ++i)
								{
									if ((board.at(i, l).getType() != BLANK && i != k) || board.at(i, l).getColor() == piece.getColor())
									{
										return false;
									}
								}
							}
							else
							{
								for (int i = i2 - 1; i >= k; --i)
								{
									if ((board.at(i, l).getType() != BLANK && i != k) || board.at(i, l).getColor() == piece.getColor())
									{
										return false;
									}
								}
							}
						}
						return true;
					case KNIGHT:
						if (board.at(k, l).getColor() == piece.getColor())
						{
							return false;
						}
						return true;
					case BISHOP:
						if (i2 < k)
						{
							if (j2 < l)
							{
								for (int i = i2 + 1, j = j2 + 1; i <= k && i != 0 && j <= l; ++i, ++j)
								{
									if ((board.at(i, j).getType() != BLANK && i != k && j != l) || board.at(i, j).getColor() == piece.getColor())
									{
										return false;
									}
								}
							}
							else
							{
								for (int i = i2 + 1, j = j2 - 1; i <= k && i != 0 && j >= l; ++i, --j)
								{
									if ((board.at((short) i, j).getType() != BLANK && i != k && j != l) || board.at(i, j).getColor() == piece.getColor())
									{
										return false;
									}
								}
							}
						}
						else
						{
							if (j2 < l)
							{
								for (int i = i2 - 1, j = j2 + 1; i >= k && i != 0 && j <= l; --i, ++j)
								{
									if ((board.at((short) i, j).getType() != BLANK && i != k && j != l) || board.at((short) i, j).getColor() == piece.getColor())
									{
										return false;
									}
								}
							}
								else
								{
									for (int i = i2 - 1, j = j2 - 1; i >= k && j >= l; --i, --j)
									{
										if ((board.at((short) i, j).getType() != BLANK && i != k && j != l) || board.at((short) i, j).getColor() == piece.getColor())
										{
											return false;
										}
									}
								}
							}
							return true;
						case QUEEN:
							if (i2 == k)
							{
								if (j2 < l)
								{
									for (int i = j2 + 1; i <= l; ++i)
									{
										if ((board.at(k, i).getType() != BLANK && i != l) || board.at(k, i).getColor() == piece.getColor())
										{
											return false;
										}
									}
								}
								else
								{
									for (int i = j2 - 1; i >= l; --i)
									{
										if ((board.at(k, i).getType() != BLANK && i != l) || board.at(k, i).getColor() == piece.getColor())
										{
											return false;
										}
									}
								}
							}
							else
							{
								if (j2 == l)
								{
									if (i2 < k)
									{
										for (int i = i2 + 1; i <= k; ++i)
										{
											if ((board.at((short) i, l).getType() != BLANK && i != k) || board.at((short) i, l).getColor() == piece.getColor())
											{
												return false;
											}
										}
									}
									else
									{
										for (int i = i2 - 1; i >= k; --i)
										{
											if ((board.at((short) i, l).getType() != BLANK && i != k) || board.at((short) i, l).getColor() == piece.getColor())
											{
												return false;
											}
										}
									}
								}
							}
							if (i2 < k)
							{
								if (j2 < l)
								{
									for (int i = i2 + 1, j = j2 + 1; i <= k && j <= l; ++i, ++j)
									{
										if ((board.at((short) i, j).getType() != BLANK && i != k && j != l) || board.at((short) i, j).getColor() == piece.getColor())
										{
											return false;
										}
									}
								}
								else
								{
									for (int i = i2 + 1, j = j2 - 1; i <= k && j >= l; ++i, --j)
									{
										if ((board.at((short) i, j).getType() != BLANK && i != k && j != l) || board.at((short) i, j).getColor() == piece.getColor())
										{
											return false;
										}
									}
								}
							}
							else
							{
								if (j2 < l)
								{
									for (int i = i2 - 1, j = j2 + 1; i >= k && j <= l; --i, ++j)
									{
										if ((board.at((short) i, j).getType() != BLANK && i != k && j != l) || board.at((short) i, j).getColor() == piece.getColor())
										{
											return false;
										}
									}
								}
								else
								{
									for (int i = i2 - 1, j = j2 - 1; i >= k && j >= l; --i, --j)
									{
										if ((board.at((short) i, j).getType() != BLANK && i != k && j != l) || board.at((short) i, j).getColor() == piece.getColor())
										{
											return false;
										}
									}
								}
							}
							return true;
						case KING:
							if (board.at(k, l).getColor() == piece.getColor())
							{
								return false;
							}
							if (i2 + 1 < k || i2 - 1 > k)
							{
								if (i2 > k)
								{
									for (int i = i2 - 1; i > 0; --i)
									{
										if (board.at((short) i, 7).getColor() != CLEAR)
										{
											return false;
										}
									}
								}
								if (i2 < k)
								{
									for (int i = i2 + 1; i < 7; ++i)
									{
										if (board.at((short) i, 7).getColor() != CLEAR)
										{
											return false;
										}
									}
								}
							}
							return true;
						}
						return false;
				}
						
				
				
				
						private boolean putInCheck(ChessBoard board, short color, int i2, int j2, int k, int m)
						{
							short[] kingLoc = new short[2];
							short enemy;
							if (color == BLACK)
							{
								enemy = WHITE;
							}
							else
							{
								enemy = BLACK;
							}
							ChessPiece target;
							target = new ChessPiece(board.at(k, m));
							board.move(i2, j2, k, m, CHECK_MOVE);
							if (i2 != k || j2 != m)
							{
								board.at(k, m).decrementMoves();
							}
							board.flip();
							getKingLocation(board, color, kingLoc);
							for (int i = 0; i < 8; ++i)
							{
								for (int j = 0; j < 8; ++j)
								{
									if (board.at(i, j).getColor() == enemy && inRange(board, board.at(i, j), i, j, kingLoc[0], kingLoc[1]) && pathClear(board, board.at(i, j), i, j, kingLoc[0], kingLoc[1]))
									{
										board.flip();
										board.move(k, m, i2, j2, CHECK_MOVE);
										if (i2 != k || j2 != m)
										{
											board.at(i2, j2).decrementMoves();
										}
										board.replace(target, k, m);
										return true;
									}
								}
							}
							board.flip();
							board.move(k, m, i2, j2, CHECK_MOVE);
							if (i2 != k || j2 != m)
							{
								board.at(i2, j2).decrementMoves();
							}
							board.replace(target, k, m);
							return false;
						}

						private void replace(ChessPiece target, int k, int m) {
							// TODO Auto-generated method stub
							
						}


						private void move(int i2, int j2, int k, int m, short checkMove) {
							// TODO Auto-generated method stub
							
						}


						/* This function overloads the previous, and calls the previous with the same
						* beginning and ending coordinates of the king.  This determines if a player
						* is currently IN check.
						*/
						
						
						
						
						private boolean putInCheck(ChessBoard board, short color)
						{
							short[] kingLoc = new short[2];
							getKingLocation(board, color, kingLoc);
							return putInCheck(board, color, kingLoc[0], kingLoc[1], kingLoc[0], kingLoc[1]);
						}

						/* This function is used by the AI implementation to decide if a certain piece
						* at a location is at risk of being lost to the opponent.
						*/

						private boolean inJeopardy(ChessBoard board, short color, int i2, int j2, int i3, int j3)
						{
							short enemy;
							if (color == BLACK)
							{
								enemy = WHITE;
							}
							else
							{
								enemy = BLACK;
							}
							ChessPiece target;
							target = new ChessPiece(board.at((short) i3, j3));
							board.move(i2, j2, i3, j3, CHECK_MOVE);
							if (i2 != i3 || j2 != j3)
							{
								board.at(i3, j3).decrementMoves();
							}
							board.flip();
							for (int i = 0; i < 8; ++i)
							{
								for (int j = 0; j < 8; ++j)
								{
									if (board.at(i, j).getColor() == enemy && inRange(board, board.at((short) i, j), i, j, 7 - i3, 7 - j3) && pathClear(board, board.at(i, j), i, j, 7 - i3, 7 - j3))
									{
										board.flip();
										board.move(i3, j3, i2, j2, CHECK_MOVE);
										if (i2 != i3 || j2 != j3)
										{
											board.at(i2, j2).decrementMoves();
										}
										board.replace(target, i3, j3);
										return true;
									}
								}
							}
							board.flip();
							board.move(i3, j3, i2, j2, CHECK_MOVE);
							if (i2 != i3 || j2 != j3)
							{
								board.at(i2, j2).decrementMoves();
							}
							board.replace(target, i3, j3);
							return false;
						}
						
						
						private boolean isStalemate(ChessBoard board, short color)
						{
							for (int i = 0; i < 8; ++i)
							{
								for (int j = 0; j < 8; ++j)
								{
									if (board.at(i, j).getColor() == color)
									{
										for (int k = 0; k < 8; ++k)
										{
											for (int m = 0; m < 8; ++m)
											{
												if (inRange(board, board.at(i, j), i, j, k, m) && pathClear(board, board.at(i, j), i, j, k, m) && !putInCheck(board, color, i, j, k, m))
												{
													return false;
												}
											}
										}
									}
								}
							}
							return true;
						}

						/* This function checks if the player is IN check, and uses the stalemate
						* function to determine whether or not there is a way out of it.  If there
						* is no way out, it is a check mate.
						*/

						private boolean isCheckMate(ChessBoard board, short color)
						{
							return putInCheck(board, color) && isStalemate(board, color);
						}

						/* This function finds a player's king piece and fills the array "loc" with
						* its location
						*/


						private void getKingLocation(ChessBoard board, short color, short[] loc)
						{
							for (int i = 0; i < 8; ++i)
							{
								for (int j = 0; j < 8; ++j)
								{
									if (board.at(i, j).getType() == Type.KING && board.at(i, j).getColor() == color)
									{
										loc[0] = (short) i;
										loc[1] = (short) j;
									}
								}
							}
						}

						/* This function returns true if there are not enough pieces left on the board
						* to accomplish a check mate.  It is used to determine if the game is a draw
						*/

						private boolean isInsufficientPieces(ChessBoard board)
						{
							if ((board.m_pieceMaps[BLACK].length == 2 && board.m_pieceMaps[WHITE].length == 2) && board.m_pieceMaps[BLACK].length != board.m_pieceMaps[BLACK].length && board.m_pieceMaps[WHITE].length != board.m_pieceMaps[WHITE].length && sameSquareColorBishops(board))
							{
								return true;
							}
							if (board.m_pieceMaps[BLACK].length + board.m_pieceMaps[WHITE].length > 3)
							{
								return false;
							}
							boolean[] insufficient = new boolean[2];
							insufficient[BLACK] = false;
							insufficient[WHITE] = false;
							for (short i = 0; i < 2; ++i)
							{
								if (board.m_pieceMaps[i].length == 1)
								{
									insufficient[i] = true;
									continue;
								}
								if (board.m_pieceMaps[i].length != board.m_pieceMaps[i].length)
								{
									insufficient[i] = true;
									continue;
								}
								if (board.m_pieceMaps[i].length != board.m_pieceMaps[i].length)
								{
									insufficient[i] = true;
									continue;
								}
							}
							return insufficient[BLACK] && insufficient[WHITE];
						}

						private boolean isDraw(ChessBoard board)
						{
							if (isInsufficientPieces(board))
							{
								System.out.print("Insufficient pieces to win game.");
								System.out.print("\n");
								return true;
							}
							return false;
						}

						/* This function is used to determine if the players both have bishops on the
						* same colored square.  It is used to determine if there are sufficient
						* pieces to accomplish a check mate.  2 kings and 2 bishops of the same color
						* square do not allow check mate.
						*/

						private boolean sameSquareColorBishops(ChessBoard board)
						{
							int xpos = -1;
							int ypos = 0;
							for (int i = 0; i < 8; ++i)
							{
								for (int j = 0; j < 8; ++j)
								{
									if (board.at((short) i, j).getType() == Type.BISHOP)
									{
										if (xpos == -1)
										{
											xpos = i;
											ypos = j;
										}
										else
										{
											if ((i % 2 == j % 2 && xpos % 2 == ypos % 2) || (i % 2 != j % 2 && xpos % 2 != ypos % 2))
											{
												return true;
											}
										}
									}
								}
							}
							return false;
						}

						
						private void computerMove(ChessBoard boardIn, short color)
						{
							ArrayList<Pair<Coord, Coord>> choices = new ArrayList<Pair<Coord, Coord>>(); // this vector stores possible moves
							ArrayList<Pair<Coord, Coord>> appealing = new ArrayList<Pair<Coord, Coord>>(); // this vector stores the most appealing moves to make
							Pair<Coord, Coord> movement = new Pair<Coord, Coord>();
							Coord coord = new Coord();
							ChessPiece piece;
							ChessBoard board = boardIn; // we make a copy of the passed-in board to manipulate for evaluation
							int appeal; // this variable is used to record the appeal of a possible move
							int maxAppeal = -10; // the max appeal variable will represent the most appealing move strength
							int checks = 0;
							int lastbX;
							short lastbY;
							int lasteX;
							int lasteY;
							boolean firstPass = true; // the computer will make an invisible "hypthetical" move on the
							do // ^-- first pass, then see if it can come up with an appealing move to
							{ // follow with, it is only looking 1 move ahead.
								for (int i = 0; i < 8; ++i) // on each pass, the computer looks at all possible moves it could make on
								{
									for (int j = 0; j < 8; ++j) // the board, and then tries to judge the best choices to randomly use
									{
										if (board.at((short) i, j).getColor() == color) // we're scanning the whole board, this checks if we can even move from here
										{
											coord.m_X = (short) i;
											coord.m_Y = (short) j;
											piece = new ChessPiece(board.at(i, j)); // we're making a copy of the piece at the location for properties
											movement.first = coord;
											for (int k = 0; k < 8; ++k) // these are the loops to handle potential target coordinates for the
											{
												for (int m = 0; m < 8; ++m) // next move to make
												{
													if (k == i && m == j) // if we're looking at moving to the same spot we're at, we just go
														continue; // on to the next evaluation
													appeal = 0;
													if (inRange(board, piece, i, j, k, m) && pathClear(board, piece, i, j, k, m)) // here we check if a certain move is valid...
													{
														if (piece.getType() == Type.KING && putInCheck(board, piece.getColor()) && j == m && (i - 2 == k || i + 2 == k))
														{
															; // we check if a castle is possible based on if we're in check, if in check
														}
														else // ^-- we do nothing with a castle move
														{
															if (piece.getType() == Type.KING && j == m && (i - 2 == k || i + 2 == k) && ((i - 2 == k && putInCheck(board, piece.getColor(), i, j, i - 1, m)) || (i + 2 == k && putInCheck(board, piece.getColor(), i, j, i + 1, m))))
															{
																; // ^-- we do nothing with a castle move if trying to move through check -  we check if a castle is possible based on if we'll move THROUGH check
															}
															else
															{
																if (!putInCheck(board, piece.getColor(), i, j, k, m)) // we've determined that was could make this move, now we see if we should
																{
																	if (firstPass && inJeopardy(board, piece.getColor(), i, j, i, j) && !inJeopardy(board, piece.getColor(), i, j, k, m)) // we're checking if moves will put the computer in jeopardy afterwards
																	{
																		appeal += (10 * piece.getType()); // we're grading moves based on what we will accomplish by moving there
																	}
																	appeal += 10 * board.at((short) k, m).getType();
																	if (board.at((short) i, j).getType() == Type.PAWN && m == 0)
																	{
																		appeal += (10 * QUEEN); // appeal scores are greater for higher ranking pieces in the piece enum
																	}
																	if ((!firstPass && inJeopardy(board, piece.getColor(), i, j, i, j)) || inJeopardy(board, piece.getColor(), i, j, k, m))
																	{
																		appeal -= 10 * piece.getType();
																		if (piece.getType() == Type.QUEEN)
																		{
																			appeal -= 20;
																		}
																	}
																	else
																	{
																		if (firstPass)
																		{
																			appeal += 10;
																		}
																	}
																	coord.m_X = (short) k;
																	coord.m_Y = (short) m;
																	movement.second = coord;
																	if (firstPass)
																	{
																		choices.add(movement); // this potential movement is now considered a "choice" for appeal checks
																	}
																	if (appeal > maxAppeal) // if we've found a move that is most appealing over previous judgements,
																	{ // ^-- we clear the appealing vector and add new "more appealing" choices
																		maxAppeal = appeal;
																		appealing.clear();
																	}
																	if (appeal == maxAppeal) // if the appeal of the current potential move matches the highest appeal
																	{ // found, we're going to add it to the appeal vector
																		if (firstPass)
																		{
																			movement.second.m_X = (short) k;
																			movement.second.m_Y = (short) m;
																		}
																		else
																		{
																			movement.first.m_X = (short) lastbX;
																			movement.first.m_Y = lastbY;
																			movement.second.m_X = (short) lasteX;
																			movement.second.m_Y = (short) lasteY;
																		}
																		appealing.add(movement); // we only push appealing moves onto the appropriate vector on 2nd pass
																	}
																}
															}
														}
													}
												}
											}
											piece = null;
										}
									}
								firstPass = false; // we've looped over the board once, now we can perform 2nd pass operations
								if (checks < choices.size())
								{
									board = boardIn;
									lastbX = (choices).at(checks).first.m_X;
									lastbY = choices.at(checks).first.m_Y;
									lasteX = choices.at(checks).second.m_X;
									lasteY = choices.at(checks).second.m_Y;
									board.move(lastbX, lastbY, lasteX, lasteY, CHECK_MOVE); // here's a call the the ChessBoard move function for a "test move"
								} // this sets up a hypothetical new board situation to further evaluate
								++checks;
							} while (checks < choices.size());
							if (appealing.size())
							{
								appealing = choices;
							}
							if (appealing.size()) // once we have looked over the board, it's time to make a move
							{
								int z;
								z = RandomNumbers.nextNumber() % (appealing.size());
								boardIn.move(appealing.at(z).first.m_X, appealing.at(z).first.m_Y, appealing.at(z).second.m_X, appealing.at(z).second.m_Y, REAL_MOVE); // calling ChessBoard move function with a real move, not hypothetical
								System.out.print("\nComputer Move: ");
								System.out.print((byte)(104 - appealing.at(z).first.m_X));
								System.out.print((byte)(49 + appealing.at(z).first.m_Y));
								System.out.print(" to ");
								System.out.print((byte)(104 - appealing.at(z).second.m_X));
								System.out.print((byte)(49 + appealing.at(z).second.m_Y));
								System.out.print("\n");
								piece = new ChessPiece(board.at(appealing.at(z).first.m_X, appealing.at(z).first.m_Y));
								if (piece.getType() == Type.KING && appealing.at(z).first.m_Y == appealing.at(z).second.m_Y && (appealing.at(z).first.m_X - 2 == appealing.at(z).second.m_X || appealing.at(z).first.m_X + 2 == appealing.at(z).second.m_X))
								{
									if (appealing.at(z).first.m_X > (appealing).at(z).second.m_X)
									{
										board.move(0, 7, 2 + piece.getColor(), 7);
									}
									else
									{
										board.move(7, 7, 4 + piece.getColor(), 7);
									}
								}
								piece = null;
							}
						}
					}
							
							private String allCaps(String str)
							{
								String rstr;
								for (int i = 0; i < str.length(); ++i)
								{
									rstr.push_back(Character.toUpperCase(str.charAt(i)));
								}
								return rstr;
							}

							/* This function gives a quick introduction to the game instructions.
							*/

							private void displayHelp()
							{
								System.out.print("\nThis is a simple game of Chess. You make moves by entering " + "piece coordinates\nin the format: \"a2 a4\", where a2 represents the " + "beginning coordinate of a piece\nand a4 represents the desired placement. " + "Normal chess rules apply.\nEnter 'Q' by itself to quit the game.");
								System.out.print("\n");
							}


	 // ^-- it will be used to determine if the game can be called a draw
	
	
	
	final class Pair<T1, T2>
	{
		public T1 first;
		public T2 second;

		public Pair()
		{
			first = null;
			second = null;
		}

		public Pair(T1 firstValue, T2 secondValue)
		{
			first = firstValue;
			second = secondValue;
		}

		public Pair(Pair<T1, T2> pairToCopy)
		{
			first = pairToCopy.first;
			second = pairToCopy.second;
		}
	}
	
}



	public int Time(int i) {
		// TODO Auto-generated method stub
		return 0;
	}
				}