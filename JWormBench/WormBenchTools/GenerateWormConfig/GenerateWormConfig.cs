/*
GenerateWormConfig - atomatic worm configuration file generator for WormBench
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
// File Name: GenerateWormConfig.cs
// Description: Generates a Worm configuration file.
// Organization: Barcelona Supercomputing Center
// Author: Ferad Zyulkyarov ferad.zyulkyarov[@]bsc.es
// Date: 04.02.2008
//
//-----------------------------------------------------------------------------

using System;
using System.IO;
using Bsc;


namespace Bsc.WormBench.Tools
{
    public class GenerateWormConfig
    {        
        static void Main(string[] args)
        {
            try
            {
                InitializeCommandLineParser();
                ParseArguments(args);

                int num = Int32.Parse(CommandLineArgumentParser.GetParamValue("-num"));               
                int rowsNum = Int32.Parse(CommandLineArgumentParser.GetParamValue("-rowsNum"));
                int columnsNum = Int32.Parse(CommandLineArgumentParser.GetParamValue("-columnsNum"));
                int speed = Int32.Parse(CommandLineArgumentParser.GetParamValue("-speed"));

                string headSizeRandom = CommandLineArgumentParser.GetParamValue("-headSize");
                int headSizeMin = ParseRandomMin(headSizeRandom);
                int headSizeMax = ParseRandomMax(headSizeRandom);

                string bodyLengthRandom = CommandLineArgumentParser.GetParamValue("-bodyLength");
                int bodyLengthMin = ParseRandomMin(bodyLengthRandom);
                int bodyLengthMax = ParseRandomMax(bodyLengthRandom);

                WormConfigGenerator wormConfig = new WormConfigGenerator(
                    num,
                    headSizeMin,
                    headSizeMax,
                    bodyLengthMin,
                    bodyLengthMax,
                    speed,
                    rowsNum,
                    columnsNum);

                wormConfig.GenerateWorms();

                Console.WriteLine("DONE" + Environment.NewLine);
            }
            catch (CommandLineArgumentException e)
            {
                Console.WriteLine(e.Message);
                PrintUsage();
            }
            catch (IOException e)
            {
                Console.WriteLine(e.Message);                
            }
        }

        private static void InitializeCommandLineParser()
        {
            string[] requiredArguments = {
                "-num",                
                "-headSize",
                "-bodyLength",
            };

            string[] optionalArguments = {
                "-rowsNum = 0",     // This will cause the range 16-1024 to be generated
                "-columnsNum = 0",
                "-speed = 1",
            };

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

        private static int ParseRandomMin(string randomStr)
        {
            int result = 0;
            randomStr = randomStr.ToLower();

            if (randomStr.Contains("random"))
            {
                int openBracketIndex = randomStr.IndexOf("[");
                int closeBracketIndex = randomStr.IndexOf("]");

                //
                // Some assertions
                //
                if ((openBracketIndex == -1) ||
                    (closeBracketIndex == -1) ||
                    (openBracketIndex + 3 >= closeBracketIndex))
                {
                    throw new ApplicationException("Error: random[x, y] is wirtten wrong.");
                }

                //
                // 012345678901
                // random[1, 2]
                // openBracket = 6
                // closeBracket = 11
                // tuple = substring(7, 4)
                //
                string tuple = randomStr.Substring(openBracketIndex + 1, closeBracketIndex - openBracketIndex - 1);

                string[] tokens = tuple.Split(',');

                if (tokens.Length != 2)
                {
                    throw new ApplicationException("Error: random[x, y] is wirtten wrong.");
                }

                string minValue = tokens[0].Trim();
                result = Int32.Parse(minValue);
            }
            else
            {
                result = Int32.Parse(randomStr);
            }

            return result;
        }

        private static int ParseRandomMax(string randomStr)
        {
            int result = 0;
            randomStr = randomStr.ToLower();

            if (randomStr.Contains("random"))
            {
                int openBracketIndex = randomStr.IndexOf("[");
                int closeBracketIndex = randomStr.IndexOf("]");

                //
                // Some assertions
                //
                if ((openBracketIndex == -1) ||
                    (closeBracketIndex == -1) ||
                    (openBracketIndex + 3 >= closeBracketIndex))
                {
                    throw new ApplicationException("Error: random[x, y] is wirtten wrong.");
                }

                //
                // 012345678901
                // random[1, 2]
                // openBracket = 6
                // closeBracket = 11
                // tuple = substring(7, 4)
                //
                string tuple = randomStr.Substring(openBracketIndex + 1, closeBracketIndex - openBracketIndex - 1);

                string[] tokens = tuple.Split(',');

                if (tokens.Length != 2)
                {
                    throw new ApplicationException("Error: random[x, y] is wirtten wrong.");
                }

                string maxValue = tokens[1].Trim();
                result = Int32.Parse(maxValue);
            }
            else
            {
                result = Int32.Parse(randomStr);
            }

            return result;
        }

        private static void PrintUsage()
        {
            Console.WriteLine();
            Console.WriteLine("USAGE:");
            Console.WriteLine("GenerateWormConfig.exe -num <int> -headSize <int> -bodyLength <int> -rowsNum <int> -collumnsNum <int>  [-speed <int>]");
            Console.WriteLine();
            Console.WriteLine("OPTIONS:");
            Console.WriteLine("  - num - number of worms to generate per file.");        
            Console.WriteLine("  - headSize: worm's head size; if random[x,y] random number in [x, y].");
            Console.WriteLine("  - bodyLength: the length of the worm's body; if random[x,y] random ");
            Console.WriteLine("    number in [x, y].");
            Console.WriteLine("  - rowsNum: the number of rows in the matrix; if not specified, ");
            Console.WriteLine("    columnsNum is discarded and worm files are created for the");
            Console.WriteLine("    range of 16-1024 sized square matrices.");
            Console.WriteLine("  - columnsNum: the number of collumns in the matrix.");    
            Console.WriteLine("  - speed: the speed that the worms moves; default 1.");
        }
    }
}
