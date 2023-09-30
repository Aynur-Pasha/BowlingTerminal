package basic;

import java.util.Arrays;
import java.util.List;

/**
 * The `SingleGame` class represents a game of bowling. It allows to add shots,
 * calculate scores, and keep track of game progress.
 * 
 * @author aynur
 */
public class SingleGame {

	private final int TOTAL_FRAMES = 10;
	private final int MAX_PINS = 10;
	private final String EMPTY_FRAME = "|     ";

	private int[][] shots;
	private int totalPoints;
	private int currentFrame;
	private int[] currentPoints = new int[TOTAL_FRAMES];
	private int[] currentBonus = new int[TOTAL_FRAMES]; // each value is 0, 1 or 2
	private boolean gameover = Boolean.FALSE;
	private String[] pinsInFrame = new String[TOTAL_FRAMES];
	private String[] pointsInFrame = new String[TOTAL_FRAMES];

	/**
	 * Constructs a new instance of the `SingleGame` class. Initializes the game
	 * state, including shots, total points, current frame, and frame displays.
	 */
	public SingleGame() {
		setShots(new int[][] { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 },
				{ -1, -1 }, { -1, -1 }, { -1, -1, -1 } });
		totalPoints = 0;
		currentFrame = 0;
		for (int i = 0; i < TOTAL_FRAMES; i++) {
			pinsInFrame[i] = EMPTY_FRAME;
			pointsInFrame[i] = EMPTY_FRAME;
		}
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public int[][] getShots() {
		return shots;
	}

	public int[] getCurrentPoints() {
		return currentPoints;
	}

	public void setCurrentPoints(int[] newCurrentPoints) {
		if (newCurrentPoints.length == TOTAL_FRAMES) {
			currentPoints = Arrays.copyOf(newCurrentPoints, newCurrentPoints.length);
		} else {
			throw new IllegalArgumentException("Invalid array length, should be 10, not " + newCurrentPoints.length);
		}
	}

    public void setShots(int[][] newShots) {
        if (newShots.length == TOTAL_FRAMES && newShots[0].length <= 3) {
            // Validate the dimensions of the new array
            shots = new int[TOTAL_FRAMES][3];

            // Copy the values from the new array to the internal array
            for (int i = 0; i < TOTAL_FRAMES-1; i++) {
                for (int j = 0; j < 2; j++) {
                    shots[i][j] = newShots[i][j];
                }
            }
            for (int j = 0; j < 3; j++) {
                shots[TOTAL_FRAMES-1][j] = newShots[TOTAL_FRAMES-1][j];
            }
        } else {
            throw new IllegalArgumentException("Invalid array dimensions");
        }
    }

	public String getPinsInFrame() {
		String pinsline = "";
		for (int i = 0; i < TOTAL_FRAMES; i++) {
			pinsline += pinsInFrame[i];
		}
		return pinsline + "|";
	}

	public void setPinsInFrame(String[] newPinsInFrame) {
		if (newPinsInFrame.length == TOTAL_FRAMES) {
			pinsInFrame = Arrays.copyOf(newPinsInFrame, newPinsInFrame.length);
		} else {
			throw new IllegalArgumentException("Invalid array length, should be 10, not " + newPinsInFrame.length);
		}
	}

	public String getPointsInFrame() {
		String pointsline = "";
		for (int i = 0; i < TOTAL_FRAMES; i++) {
			pointsline += pointsInFrame[i];
		}
		return pointsline + "|";
	}

	public void setPointsInFrame(String[] newPointsInFrame) {
		if (newPointsInFrame.length == TOTAL_FRAMES) {
			pointsInFrame = Arrays.copyOf(newPointsInFrame, newPointsInFrame.length);
		} else {
			throw new IllegalArgumentException("Invalid array length, should be 10, not " + newPointsInFrame.length);
		}
	}

	public int[] getCurrentBonus() {
		return currentBonus;
	}

	public void setCurrentBonus(int[] newCurrentBonus) {
		if (newCurrentBonus.length == TOTAL_FRAMES) {
			currentBonus = Arrays.copyOf(newCurrentBonus, newCurrentBonus.length);
		} else {
			throw new IllegalArgumentException("Invalid array length, should be 10, not " + newCurrentBonus.length);
		}
	}

	public boolean isGameover() {
		return gameover;
	}

	public void setGameover(boolean gameover) {
		this.gameover = gameover;
	}

	/**
	 * Checks if a given number of pins is valid for a bowling shot and if the total pins
	 * for the current frame do not exceed the maximum allowed.
	 *
	 * @param pins        The number of pins knocked down in the current shot.
	 * @param currentShot The current shot within the frame (0 for the first shot, 1 for the second).
	 * @throws InvalidPinCountException If the number of pins is not within the valid range or if
	 *                                  the total pins for the current frame exceed the maximum allowed.
	 */
	private void checkEntry(int pins, int currentShot) {
		if ((pins < 0) || (pins > MAX_PINS))
			throw new InvalidPinCountException(pins);
		if (currentShot > 0 && shots[currentFrame][0] + pins > MAX_PINS)
			throw new InvalidPinCountException(pins);
	}

	public void resetGame() {
		// Reset the game by clearing all shots and scores
		for (int i = 0; i < TOTAL_FRAMES; i++) {
			for (int j = 0; j < 2; j++) {
				getShots()[i][j] = -1;
			}
		}
		totalPoints = 0;
		currentFrame = -1;
	}

	/**
	 * Adds a bowling shot to the game, updating the game state and scores accordingly.
	 *
	 * @param pins The number of pins knocked down in the current shot.
	 * @throws IllegalStateException      If the game is already over.
	 * @throws InvalidPinCountException  If the number of pins is not within the valid range.
	 */
	public void addShot(int pins) {

		if (currentFrame > 9) {

			setGameover(Boolean.TRUE);
			throw new IllegalStateException("The game is over.");
		}

		else if ((currentFrame == 9)
				&& ((shots[9][0] == MAX_PINS) || (shots[currentFrame][0] + shots[currentFrame][1] == MAX_PINS))) {
			dealWithBonusShot(pins);
		} else {
			totalPoints += pins;
			// is this the first shot in this frame?
			int currentShot = getShots()[currentFrame][0] == -1 ? 0 : 1;

			checkEntry(pins, currentShot);
			shots[currentFrame][currentShot] = pins;
			updatePinsInFrame(currentFrame, currentShot);
			adjustTwoSteps(pins, currentShot);
			adjustOneStep(pins);

			// anyway, the last frame has the totalPoints
			currentPoints[currentFrame] = totalPoints;
			if (currentShot == 0) {

				// Did you strike?
				if (pins == MAX_PINS) {
					if (currentFrame != 9) {
						currentBonus[currentFrame] = 2;
						shots[currentFrame][1] = 0;
						updatePointsInFrame(currentFrame);
						currentFrame++;
					}
				}
			} else if (currentShot == 1) {

				// Did you spare?
				if (shots[currentFrame][0] + pins == MAX_PINS) {
					currentBonus[currentFrame] = 1;
				}

				// If this is the last frame
				if (currentFrame == 9) {
					// Will there be no bonus shot?
					if (shots[currentFrame][0] + pins != MAX_PINS) {
						gameover = Boolean.TRUE;
					} else {
						currentFrame--;
					}
				}

				updatePointsInFrame(currentFrame);

				currentFrame++;
			}
		}

	}
	
	/**
	 * Handles the bonus shots in the last frame of a bowling game, updating the game state and scores accordingly.
	 *
	 * @param pins The number of pins knocked down in the bonus shot.
	 * @throws InvalidPinCountException If the number of pins is not within the valid range.
	 */
	public void dealWithBonusShot(int pins) {

		totalPoints += pins;
		// when the first hit of the last frame is MAX_PINS
		if (shots[9][0] == MAX_PINS) {
			// second shot of the last frame
			if (shots[9][1] == -1) {
				shots[currentFrame][1] = pins;
				updatePinsInFrame(currentFrame, 1);
				adjustTwoSteps(pins, 1);
				adjustOneStep(pins);
				currentPoints[currentFrame] = totalPoints;
			}
			// third shot of the last frame
			else {
				if ((shots[currentFrame][1] + pins > MAX_PINS) && !(shots[9][1] == MAX_PINS)) {
					throw new InvalidPinCountException(pins);
				}
				shots[currentFrame][2] = pins;
				// nothing to adjust
				currentPoints[currentFrame] = totalPoints;

				updatePinsInFrame(currentFrame, 2);
				updatePointsInFrame(currentFrame);
				setGameover(Boolean.TRUE);
			}
		}
		// when the second hit of the last frame is a spare
		else {
			if (shots[9][1] == -1) {
				shots[currentFrame][1] = pins;
				currentPoints[currentFrame] = totalPoints;
				updatePinsInFrame(currentFrame, 1);
				updatePointsInFrame(currentFrame);
			} else {
				shots[currentFrame][2] = pins;
				currentPoints[currentFrame] = totalPoints;
				updatePinsInFrame(currentFrame, 2);
				updatePointsInFrame(currentFrame);
				setGameover(Boolean.TRUE);
			}
		}
	}

	/**
	 * Adjusts the scores for a strike two frames ago and updates the total points accordingly.
	 *
	 * @param pins    The number of pins knocked down in the current shot.
	 * @param shotNum The current shot within the frame (0 for the first shot, 1 for the second).
	 */
	private void adjustTwoSteps(int pins, int shotNum) {
		if (currentFrame > 0) {
			if (shotNum == 0) { // first shot in the frame
				if (currentBonus[currentFrame - 1] == 2) {
					// previous frame was a strike, look at one before
					if ((currentFrame > 1) && (currentBonus[currentFrame - 2] > 0)) {
						// Two previous frames are strikes
						currentPoints[currentFrame - 2] += pins;
						currentPoints[currentFrame - 1] += pins;
						totalPoints += pins;
						currentBonus[currentFrame - 2]--;
					}
				}
			} else if (shotNum == 1) { // second shot in the frame
				adjustOneStep(pins);

			}
		}
	}
	
	/**
	 * Adjusts the scores for a strike or spare one frame ago and updates the total points accordingly.
	 *
	 * @param pins    The number of pins knocked down in the current shot.
	 * @param shotNum The current shot within the frame (0 for the first shot, 1 for the second).
	 */
	private void adjustOneStep(int pins) {
		if (currentFrame > 0) {
			if (currentBonus[currentFrame - 1] > 0) {
				// Adjust for previous frame
				currentPoints[currentFrame - 1] += pins;
				totalPoints += pins;
				currentBonus[currentFrame - 1]--;
			}
		}
	}

	/**
	 * Updates the representation of pins knocked down in each frame for display purposes.
	 *
	 * @param frameNumber The index of the frame to be updated.
	 * @param shotNumber  The current shot within the frame (0 for the first shot, 1 for the second, 2 for bonus).
	 */
	public void updatePinsInFrame(int frameNumber, int shotNumber) {
		if (shotNumber == 0) {
			// add new frame
			if (shots[currentFrame][0] == MAX_PINS) {
				pinsInFrame[frameNumber] = "|  X  ";
			} else {
				pinsInFrame[frameNumber] = "| " + shots[currentFrame][0] + "   ";
			}
		} else if (shotNumber == 1) {
			// edit existing frame
			if (frameNumber != 9) {
				if (shots[currentFrame][0] + shots[currentFrame][1] == MAX_PINS) {
					pinsInFrame[frameNumber] = "| " + shots[currentFrame][0] + " " + "/ ";
				} else {
					pinsInFrame[frameNumber] = "| " + shots[currentFrame][0] + " " + shots[currentFrame][1] + " ";
				}
			} else {
				if (shots[currentFrame][0] == MAX_PINS) {
					if (shots[currentFrame][1] == MAX_PINS) {
						pinsInFrame[frameNumber] = "| X X ";
					} else {
						pinsInFrame[frameNumber] = "| X " + shots[currentFrame][1] + " ";
					}
				} else if (shots[currentFrame][0] + shots[currentFrame][1] == MAX_PINS) {
					pinsInFrame[frameNumber] = "| " + shots[currentFrame][0] + " / ";
				} else {
					pinsInFrame[frameNumber] = "| " + shots[currentFrame][0] + " " + shots[currentFrame][1];
				}
			}

		} else { // bonus shot
			String[] lastFrame = new String[3];
			for (int i = 0; i < 3; i++) {
				if (shots[currentFrame][i] == MAX_PINS) {
					lastFrame[i] = "X";
				} else if (shots[currentFrame][i] == -1) {
					lastFrame[i] = " ";
				} else {
					lastFrame[i] = Integer.toString(shots[currentFrame][i]);
				}
			}
			if ((shots[currentFrame][0] != MAX_PINS) && (shots[currentFrame][0] + shots[currentFrame][1] == MAX_PINS)) {
				lastFrame[1] = "/";
			}

			if ((shots[currentFrame][0] == MAX_PINS) && (shots[currentFrame][1] + shots[currentFrame][2] == MAX_PINS)) {
				lastFrame[2] = "/";
			}

			pinsInFrame[frameNumber] = "| " + lastFrame[0] + " " + lastFrame[1] + " " + lastFrame[2] + " ";

		}
	}
	
	/**
	 * Updates the representation of total points scored in each frame for display purposes.
	 *
	 * @param frameNumber The index of the frame to be updated.
	 */
	public void updatePointsInFrame(int frameNumber) {

		pointsInFrame[frameNumber] = "| " + String.format("%1$3s", currentPoints[currentFrame]) + " ";
	}
}
