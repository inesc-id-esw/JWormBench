/*    
CommandLineArgumentParser Copyright (C) 2008 Ferad Zyulkyarov

This library is free software; you can redistribute it and/or modify it under 
the terms of the GNU Lesser General Public License as published by the Free
Software Foundation; either version 2.1 of the License, or (at your option) any
later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along
with this library; if not, write to the Free Software Foundation, Inc., 
59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
*/

//+----------------------------------------------------------------------------
//
// File Name: ExampleUsage.cs
// Description: Demonstrates how to use CommandLineArgumentParser.
// Author: Ferad Zyulkyarov ferad.zyulkyarov[@]bsc.es
// Date: 04.02.2008
//
//-----------------------------------------------------------------------------

using System;
using Bsc;


/// <summary>
/// Demonstrates how to use CommandLineArgumentParser.
/// 
/// Usage:
/// 1. Make a reference file Bsc.CommandLineArgumentParser.dll.
///    In solution explorer, right click on "References" and select "Add Reference" 
///    from the context menu. Then click on the "Browse" tab and navigate to 
///    Bsc.CommandLineArgumentParser.dll.
/// 2. Include the Bsc namespace.
///    using Bsc.
/// </summary>
public class ExampleUsage
{
    public static void Main(string[] args)
    {
        //
        // Command line arguments parser throws very descriptive information
        // when an error happens parsing the arguments. It is strongly
        // recommended to catch CommandLineArgumentException exception when
        // calling CommandLineArgumentParser methods.
        //
        try
        {
            //
            // Initialize the command line argument's parser by providing
            // the expected, optional parameters and application switches.
            //
            InitializeComandLineParser();

            //
            //  Parse the command line arguments.
            //
            ParseArguments(args);

            //
            // Test evey arguments if assigned properly.
            //
            TestArguments();
        }
        catch (CommandLineArgumentException e)
        {
            Console.WriteLine(e.Message);
            PrintUsage();
        }
    }

    private static void InitializeComandLineParser()
    {
        //
        // Define the required parameters.
        //
        string[] requiredArguments = {
            "-required1",
            "-required2",
        };
        CommandLineArgumentParser.DefineRequiredParameters(requiredArguments);
        
        //
        // Define the optional parameters along with their default values.
        //
        string[] optionalArguments = {
            "-optional1 = 2",
            "-otpional2 = String",
        };
        CommandLineArgumentParser.DefineOptionalParameter(optionalArguments);

        //
        // Define the supported switches.
        //
        string[] switches = {
            "-switch1",
            "-switch2",
        };
        CommandLineArgumentParser.DefineSwitches(switches);
    }

    private static void ParseArguments(string[] args)
    {
        //
        // Handle the special case "-help" separately
        //
        if (args.Length == 1 && args[0].Trim() == "-help")
        {
            PrintUsage();
        }
        else
        {
            CommandLineArgumentParser.ParseArguments(args);
        }
    }

    private static void TestArguments()
    {
        //
        // Read the values of the parameters.
        //

        int required1 = Int32.Parse(CommandLineArgumentParser.GetParamValue("-required1"));
        float required2 = float.Parse(CommandLineArgumentParser.GetParamValue("-required2"));
        int optional1 = Int32.Parse(CommandLineArgumentParser.GetParamValue("-optional1"));
        string optional2 = CommandLineArgumentParser.GetParamValue("-optional2");
        bool switch1 = Boolean.Parse(CommandLineArgumentParser.GetParamValue("-switch1"));
        bool switch2 = Boolean.Parse(CommandLineArgumentParser.GetParamValue("-switch2"));
    }

    private static void PrintUsage()
    {
        Console.WriteLine();
        Console.WriteLine("USAGE:");
        Console.WriteLine("ExampleUsage.exe -required1 <int> -required2 <float>");
        Console.WriteLine("    [-optional1 <int>] [-otpional2 <string>] [-switch1] [-switch2]");
        Console.WriteLine();
        Console.WriteLine("OPTIONS:");
        Console.WriteLine("  - required1: is a required parameter.");
        Console.WriteLine("  - required2: is a required parameter.");
        Console.WriteLine("  - optional1: is an optional parameter; default 2.");
        Console.WriteLine("  - optional2: the groupId; default String.");
        Console.WriteLine("  - switch1: is a switch that when specified trigers some feature on.");
        Console.WriteLine("  - switch2: is a switch that when specified trigers some feature on.");
        Console.WriteLine();
        Console.WriteLine("EXAMPLE:");
        Console.WriteLine("1. > ExampleUsage.exe -required1 150 -required2 2.34");
        Console.WriteLine("   - Specify only the requred paramters.");
        Console.WriteLine();
        Console.WriteLine("2. > ExampleUsage.exe -required1 150 -required1 2.34 -optional1 7 -switch1");
        Console.WriteLine("   - Specify the requred parameters, set value 7 to the optional1 paramter");
        Console.WriteLine("     and turn on the switch1");
    }
}
