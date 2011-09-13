/*
OperationsGenerator - atomatic operations file generator for WormBench
Copyright (C) 2008  Barcelona Supercomputing Center www.bscmsrc.eu

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

//+----------------------------------------------------------------------------
//
// File Name: OperationsGenerator.cs
// Description: A WormBench tool application that generates an operations
//              file.
// Organization: Barcelona Supercomputing Center
// Author: Ferad Zyulkyarov ferad.zyulkyarov[@]bsc.es
// Date: 26.02.2008
//
//-----------------------------------------------------------------------------

using System;
using Bsc;
using System.IO;

namespace Bsc.WormBench.Tools
{
    /// <summary>
    /// Console application that generates operations file.    
    /// </summary>
    class OperationsGenerator
    {        
        static void Main(string[] args)
        {
            try
            {
                InitializeCommandLineParser();
                ParseArguments(args);

                int operationsNum = Int32.Parse(CommandLineArgumentParser.GetParamValue("-num"));
                string operationTypes = CommandLineArgumentParser.GetParamValue("-opTypes");
                string operationsRatio = CommandLineArgumentParser.GetParamValue("-ratio"); //added by FMC
                string outputFileName = CommandLineArgumentParser.GetParamValue("-out");

                OperationsGeneratorHandler handler = new OperationsGeneratorHandler(
                    operationsNum,
                    operationTypes,
                    operationsRatio,
                    outputFileName);

                handler.GenerateRandom();

                Console.WriteLine("{0} - operations file created.", outputFileName);
                Console.WriteLine("DONE");
            }
            catch (CommandLineArgumentException e)
            {
                Console.WriteLine(e.Message);
                PrintUsage();
                return;
            }
            catch (IOException e)
            {
                Console.WriteLine(e.Message);                
            }

        }

        static void InitializeCommandLineParser()
        {
            string[] requiredArguments = {
                "-num",
                "-opTypes"};

            string[] optionalArguments = {
                "-ratio = 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1", // added by FMC
                "-out = Operations.txt" };

            CommandLineArgumentParser.DefineRequiredParameters(requiredArguments);
            CommandLineArgumentParser.DefineOptionalParameter(optionalArguments);
        }

        private static void ParseArguments(string[] args)
        {
            if (args.Length == 1 && args[0].Trim() == "-help")
            {
                PrintUsage();
            }
            else
            {
                CommandLineArgumentParser.ParseArguments(args);
            }
        }

        private static void PrintUsage()
        {
            Console.WriteLine();
            Console.WriteLine("USAGE:");
            Console.WriteLine("OperationsGenerator.exe -num <int> -opTypes <string> [-out <string>]");
            Console.WriteLine();
            Console.WriteLine("OPTIONS:");
            Console.WriteLine("  - num: the total number operations to generate.");
            Console.WriteLine("  - opTypes: space separated list of the possible operations.");
            Console.WriteLine("  - out: the name of the output file; default Operations.txt.");
            Console.WriteLine();
            Console.WriteLine("EXAMPLE:");
            Console.WriteLine("   > OperationsGenerator.exe -num 1600000 -opTypes \"0 1 4 7\" -out Output.txr");
            Console.WriteLine("   - Creates 1600000 operation out of the operation types denoted as '0 1 4 7'.");
        }
    }
}
