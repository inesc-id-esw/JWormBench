package jwormbench.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandLineArgumentParser {
  /**
   * The character used to distinguish which command lines parameters.
   */
  private final String PARAM_SEPARATOR = "-";
  /**
   * Stores the name=value required parameters.
   */
  private static Map<String, String> requiredParameters;
  /**
   * Optional parameters.
   */
  private static Map<String, String> optionalParameters;
  /**
   * Store the list of missing required parameters.
   */
  private static List<String> missingRequiredParameters;

  /**
   * Store the list of missing values of parameters.
   */
  private static List<String> missingValue;
  /**
   * Contains the raw arguments.
   */
  private static List<String> rawArguments;

  /**
   * Define the required parameters that the user of the program must provide.
   * 
   * @param requiredParameterNames
   *          The list of the required parameters.
   */
  public static void DefineRequiredParameters(String[] requiredParameterNames) {
    CommandLineArgumentParser.requiredParameters = new HashMap<String, String>();
    for (String param : requiredParameterNames) {
      if (param == null || param.isEmpty()) {
        String errorMessage = String.format(
            "Error: The required command line parameter '%s' is empty.", param);
        throw new CommandLineArgumentException(errorMessage);
      }
      String temp = param;
      temp = temp.trim();
      CommandLineArgumentParser.requiredParameters.put(param, "");
    }
  }

  /**
   * Define the optional parameters. The parameters must be provided with their
   * default values in the following format "paramName=paramValue".
   * 
   * @param optionalPerams
   *          The list of the optional parameters with their default values.
   */
  public static void DefineOptionalParameter(String[] optionalPerams) {
    CommandLineArgumentParser.optionalParameters = new HashMap<String, String>();
    for (String param : optionalPerams) {
      String[] tokens = param.split("=");
      if (tokens.length != 2) {
        String errorMessage = String
            .format(
                "Error: The optional command line parameter '%s' has wrong format.\n Expeted param=value.",
                param);
        throw new CommandLineArgumentException(errorMessage);
      }
      tokens[0] = tokens[0].trim();
      if (tokens[0] == null || tokens[0].isEmpty()) {
        String errorMessage = String.format(
            "Error: The optional command line parameter '%s' has empty name.",
            param);
        throw new CommandLineArgumentException(errorMessage);
      }
      tokens[1] = tokens[1].trim();
      if (tokens[1] == null || tokens[1].isEmpty()) {
        String errorMessage = String.format(
            "Error: The optional command line parameter '%s' has no value.",
            param);
      }
      CommandLineArgumentParser.optionalParameters.put(tokens[0], tokens[1]);
    }
  }

  /**
   * Parse the command line arguments.
   * 
   * @param args
   *          The command line arguments
   */
  public static void ParseArguments(String[] args) {
    rawArguments = Arrays.asList(args);
    missingRequiredParameters = new LinkedList<String>();
    missingValue = new LinkedList<String>();

    ParseRequiredParameters();
    ParseOptionalParameters();
    ThrowIfErrors();
  }

  /**
   * Returns the value of the specified parameter.
   * 
   * @param paramName
   *          The value of the perameter.
   */
  public static String GetParamValue(String paramName) {
    String paramValue = "";

    if (requiredParameters != null && requiredParameters.containsKey(paramName)) {
      paramValue = requiredParameters.get(paramName);
    } else if (optionalParameters != null && optionalParameters.containsKey(paramName)) {
      paramValue = optionalParameters.get(paramName);
    } else {
      String errorMessage = String.format(
          "Error: The paramter '%s' is not supported.", paramName);
      throw new CommandLineArgumentException(errorMessage);
    }

    return paramValue;
  }

  private static void ParseRequiredParameters() {
    if (CommandLineArgumentParser.requiredParameters == null
        || CommandLineArgumentParser.requiredParameters.size() == 0) {
      return;
    }

    Set<String> paramNames = CommandLineArgumentParser.requiredParameters
        .keySet();
    for (String paramName : paramNames) {

      int paramInd = rawArguments.indexOf(paramName);
      if (paramInd < 0) {
        missingRequiredParameters.add(paramName);
      } else {
        if (paramInd + 1 < rawArguments.size()) {
          //
          // The argument after the parameter name is expected to be its value.
          // No check for error is done here.
          //
          requiredParameters.put(paramName, rawArguments.get(paramInd + 1));

          rawArguments.remove(paramInd);
          rawArguments.remove(paramInd);
        } else {
          missingValue.add(paramName);
          rawArguments.remove(paramInd);
        }
      }
    }
  }

  private static void ParseOptionalParameters() {
    if (CommandLineArgumentParser.optionalParameters == null
        || CommandLineArgumentParser.optionalParameters.size() == 0) {
      return;
    }

    Set<String> paramNames = CommandLineArgumentParser.optionalParameters
        .keySet();
    for (String paramName : paramNames) {
      int paramInd = rawArguments.indexOf(paramName);
      if (paramInd >= 0) {
        if (paramInd + 1 < rawArguments.size()) {
          optionalParameters.put(paramName, rawArguments.get(paramInd + 1));

          rawArguments.remove(paramInd);

          //
          // After removing the param name, the index of the value
          // becomes again paramInd.
          //
          rawArguments.remove(paramInd);
        } else {
          missingValue.add(paramName);
          rawArguments.remove(paramInd);
        }
      }
    }
  }

  private static void ThrowIfErrors() {
    StringBuilder errorMessage = new StringBuilder();

    if (missingRequiredParameters.size() > 0 || missingValue.size() > 0
        || rawArguments.size() > 0) {
      errorMessage.append("Error: Processing Command Line Arguments\n");
    }

    if (missingRequiredParameters.size() > 0) {
      errorMessage.append("Missing Required Parameters\n");
      for (String missingParam : missingRequiredParameters) {
        errorMessage.append("\t" + missingParam + "\n");
      }
    }

    if (missingValue.size() > 0) {
      errorMessage.append("Missing Values\n");
      for (String value : missingValue) {
        errorMessage.append("\t" + value + "\n");
      }
    }

    if (rawArguments.size() > 0) {
      errorMessage.append("Unknown Parameters");
      for (String unknown : rawArguments) {
        errorMessage.append("\t" + unknown + "\n");
      }
    }
    if (errorMessage.length() > 0) {
      throw new CommandLineArgumentException(errorMessage.toString());
    }
  }
}
