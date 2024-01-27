package com.joq.keywords;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

import com.joq.exception.AutomationException;
import com.joq.utils.AutomationConstants;

public class Utilities {

	public Random random;

	/**
	 * Method to get a random number between the two ranges
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @param lowerBound
	 * @param upperBound
	 * @throws AutomationException
	 */
	public int getRandomNumber(int lowerBound, int upperBound) throws AutomationException {
		int randomNum = 0;
		try {
			random = new Random();
			randomNum = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return randomNum;
	}

	/**
	 * Method to get a random number with the a number length mentioned
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @param numberLength
	 * @throws AutomationException
	 */
	public String getRandomNumber(int numberLength) throws AutomationException {
		String randomNumber = null;
		try {
			random = new Random();
			int randomNum = 0;
			boolean loop = true;
			while (loop) {
				randomNum = random.nextInt();
				if (Integer.toString(randomNum).length() == numberLength
						&& !Integer.toString(randomNum).startsWith("-")) {
					loop = false;
				}
			}
			randomNumber = Integer.toString(randomNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return randomNumber;
	}

	/**
	 * Method to get a random string value with the string length mentioned
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @param stringLength
	 * @throws AutomationException
	 */
	public String getRandomString(int stringLength) throws AutomationException {
		try {
			String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
			StringBuilder sb = new StringBuilder(stringLength);
			for (int i = 0; i < stringLength; i++) {
				int index = (int) (AlphaNumericString.length() * Math.random());
				sb.append(AlphaNumericString.charAt(index));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to get a random string which has only alphabets with the string length
	 * mentioned
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @param stringLength
	 * @throws AutomationException
	 */
	public String getRandomStringOnlyAlphabets(int stringLength) throws AutomationException {
		try {
			String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
			StringBuilder sb = new StringBuilder(stringLength);
			for (int i = 0; i < stringLength; i++) {
				int index = (int) (AlphaNumericString.length() * Math.random());
				sb.append(AlphaNumericString.charAt(index));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to get the current date
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @throws AutomationException
	 */
	public String getCurrentDate() throws AutomationException {
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss");
			Date date = new Date();
			String filePathdate = dateFormat.format(date).toString();
			return filePathdate;
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to get the current date in the date format ddMMMyyyy
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @throws AutomationException
	 */
	public String getCurrentDateInFormatddMMMyyyy() throws AutomationException {
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
			Date date = new Date();
			String filePathdate = dateFormat.format(date).toString();
			return filePathdate;
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to get a current date in the date format ddMMyyyy
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @throws AutomationException
	 */
	public String getCurrentDateInFormatddMMyyyy() throws AutomationException {
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String filePathdate = dateFormat.format(date).toString();
			return filePathdate;
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to get the day from the current date
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @throws AutomationException
	 */
	public String getDayFromCurrentDate() throws AutomationException {
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss");
			Date date = new Date();
			String filePathdate = dateFormat.format(date).toString();
			String day = filePathdate.substring(0, 2);
			return day;
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to convert a double value to an Integer
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @param doubleValue
	 * @throws AutomationException
	 */
	public int convertDoubleToInt(double doubleValue) throws AutomationException {
		try {
			int intValue = (int) doubleValue;
			return intValue;
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to convert a float value to an Integer
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @param floatValue
	 * @throws AutomationException
	 */
	public int convertFloatToInt(float floatValue) throws AutomationException {
		try {
			int intValue = (int) floatValue;
			return intValue;
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to convert a string value to an Integer
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @param stringValue
	 * @throws AutomationException
	 */
	public int convertStringToInt(String stringValue) throws AutomationException {
		try {
			int intValue = Integer.parseInt(stringValue);
			return intValue;
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to convert a string value to a double value
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @param stringValue
	 * @throws AutomationException
	 */
	public double convertStringToDouble(String stringValue) throws AutomationException {
		try {
			double doubleValue = Double.parseDouble(stringValue);
			return doubleValue;
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to convert an Integer to a string value
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @param intValue
	 * @throws AutomationException
	 */
	public String convertIntToString(int intValue) throws AutomationException {
		try {
			String stringValue = String.valueOf(intValue);
			return stringValue;
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to convert a double value to a string value
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @param doubleValue
	 * @throws AutomationException
	 */
	public String convertDoubleToString(double doubleValue) throws AutomationException {
		try {
			String stringValue = String.valueOf(doubleValue);
			return stringValue;
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to convert a string value to a long value
	 * 
	 * @author sanoj.swaminathan
	 * @since 20-04-2020
	 * @modified 31-03-2023
	 * @param doubleValue
	 * @throws AutomationException
	 */
	public long convertStringToLong(String stringValue) throws AutomationException {
		try {
			long longValue = Long.parseLong(stringValue);
			return longValue;
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
	}

	/**
	 * Method to encode any file data
	 * 
	 * @author sanoj.swaminathan
	 * @since 06-03-2023
	 * @modified 31-03-2023
	 * @param filePath
	 * @throws AutomationException
	 */
	public String encodeFile(String filePath) throws AutomationException {
		String encodedString = null;
		try {
			byte[] fileContent = Files.readAllBytes(new File(filePath).toPath());
			encodedString = Base64.getEncoder().encodeToString(fileContent);
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
		return encodedString;
	}

	/**
	 * Method to encode strings
	 * 
	 * @author sanoj.swaminathan
	 * @since 13-03-2021
	 * @param stringToEncode
	 * @return
	 * @throws AutomationException
	 */
	public String encodeStrings(final String stringToEncode) throws AutomationException {
		byte[] encoded;
		try {
			encoded = Base64.getEncoder().encode(stringToEncode.getBytes());
		} catch (final Exception lException) {
			throw new AutomationException(getExceptionMessage(), lException);
		}
		return new String(encoded);
	}

	/**
	 * Method to decode any string data
	 * 
	 * @author sanoj.swaminathan
	 * @since 06-03-2023
	 * @modified 31-03-2023
	 * @param dataToBeDecoded
	 * @throws AutomationException
	 */
	public String decodeStrings(String dataToBeDecoded) throws AutomationException {
		byte[] decodedString = null;
		try {
			decodedString = Base64.getDecoder().decode(dataToBeDecoded);
		} catch (Exception e) {
			throw new AutomationException(getExceptionMessage() + "\n" + AutomationConstants.CAUSE + e.getMessage());
		}
		return decodedString.toString();
	}

	/**
	 * Method to delete file
	 * 
	 * @author sanoj.swaminathan
	 * @since 21-03-2023
	 * @modified 31-03-2023
	 * @param filePath
	 */
	public void deleteFile(String filePath) {
		try {
			Files.delete(new File(filePath).toPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to get the Exception message, to pass the message whenever an
	 * exception is encountered
	 * 
	 * @author sanoj.swaminathan
	 * @since 13-04-2020
	 * @modified 31-03-2023
	 */
	public String getExceptionMessage() {
		StringBuffer message = new StringBuffer();
		try {
			message.append("Exception in ");
			message.append(Thread.currentThread().getStackTrace()[2].getClassName());
			message.append(".");
			message.append(Thread.currentThread().getStackTrace()[2].getMethodName());
			message.append("()");
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return message.toString();
	}
}
