/*
RandomBenchWorldGenerator - Random BenchWorld generator for WormBench.
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
// File Name: RandomBenchWorldGenerator.cs
// Description: Generates a random BenchWorld config file.
// Organization: Barcelona Supercomputing Center
// Author: Ferad Zyulkyarov ferad.zyulkyarov[@]bsc.es
// Date: 03.02.2008
//
//-----------------------------------------------------------------------------

using System;
using System.IO;

namespace Bsc.WormBench.Tools
{
    class RandomBenchWorldGenerator
    {
        private static int rowsNum = 32;
        private static int columnsNum = 32;
        private static int minRange = 0;
        private static int maxRange = 9;
        private static string outFile = "BenchWorldConfig.txt";

        static void Main(string[] args)
        {
            bool parseSuccess = ParseArguments(args);          

            if (!parseSuccess)
            {
                PrintUsage();
                return;
            }

            Console.WriteLine("Configuration file for BenchWorld: " + outFile);
            Console.WriteLine("Matrix: {0}x{1}", rowsNum, columnsNum);
            Console.WriteLine("Range: [{0}, {1}]", minRange, maxRange);                       

            Random rand = new Random((int)DateTime.Now.ToFileTimeUtc());
            int temp = 0;

            string[] lines = new string[rowsNum + 1];
            lines[0] = rowsNum + "x" + columnsNum;

            for (int i = 1; i < rowsNum + 1; i++)
            {
                for (int j = 0; j < columnsNum; j++)
                {
                    if (minRange < 0)
                    {
                        temp = rand.Next(maxRange + Math.Abs(minRange));
                    }
                    else if (minRange > 0)
                    {
                        temp = rand.Next(maxRange) % minRange;
                    }
                    else
                    {
                        temp = rand.Next(maxRange);
                    }

                    lines[i] = lines[i] + temp + " ";
                }
            }

            try
            {
                using (StreamWriter sw = new StreamWriter(outFile))
                {
                    foreach (string line in lines)
                    {
                        sw.WriteLine(line);
                    }
                }
            }
            catch (IOException e)
            {
                Console.WriteLine("Error: {0}", e.Message);
            }
        }

        private static bool ParseArguments(string[] args)
        {
            bool parseSuccess = true;

            for (int i = 0; i < args.Length; i = i + 2)
            {
                switch (args[i])
                {
                    case "-rows":
                        {
                            if (i + 1 < args.Length)
                            {
                                try
                                {
                                    rowsNum = Int32.Parse(args[i + 1]);
                                }
                                catch (FormatException e)
                                {
                                    Console.WriteLine("Error: parsing value for '{0}' - {1}", args[i], e.Message);
                                    parseSuccess = false;
                                }
                            }
                            else
                            {
                                Console.WriteLine("Error: no matching value for command line parameter '{0}'", args[i]);
                                parseSuccess = false;
                            }
                        } break ;
                    case "-columns":
                        {
                            if (i + 1 < args.Length)
                            {
                                try
                                {
                                    columnsNum = Int32.Parse(args[i + 1]);
                                }
                                catch (FormatException e)
                                {
                                    Console.WriteLine("Error: parsing value for '{0}' - {1}", args[i], e.Message);
                                    parseSuccess = false;
                                }
                            }
                            else
                            {
                                Console.WriteLine("Error: no matching value for command line parameter '{0}'", args[i]);
                                parseSuccess = false;
                            }
                        } break;
                    case "-min":
                        {
                            if (i + 1 < args.Length)
                            {
                                try
                                {
                                    minRange = Int32.Parse(args[i + 1]);
                                }
                                catch (FormatException e)
                                {
                                    Console.WriteLine("Error: parsing value for '{0}' - {1}", args[i], e.Message);
                                    parseSuccess = false;
                                }
                            }
                            else
                            {
                                Console.WriteLine("Error: no matching value for command line parameter '{0}'", args[i]);
                                parseSuccess = false;
                            }
                        } break;
                    case "-max":
                        {
                            if (i + 1 < args.Length)
                            {
                                try
                                {
                                    maxRange = Int32.Parse(args[i + 1]);
                                }
                                catch (FormatException e)
                                {
                                    Console.WriteLine("Error: parsing value for '{0}' - {1}", args[i], e.Message);
                                    parseSuccess = false;
                                }
                            }
                            else
                            {
                                Console.WriteLine("Error: no matching value for command line parameter '{0}'", args[i]);
                                parseSuccess = false;
                            }
                        } break;
                    case "-out":
                        {
                            if (i + 1 < args.Length)
                            {
                                outFile = args[i + 1];
                            }
                            else
                            {
                                Console.WriteLine("Error: no matching value for command line parameter '{0}'", args[i]);
                                parseSuccess = false;
                            }
                        } break;
                    case "-help":
                        {
                            return false;
                        };
                    default:
                        {
                            Console.WriteLine("Error: unknown parameter '{0}'", args[i]);
                            parseSuccess = false;
                        } break ;
                };
            }

            return parseSuccess;
        }

        private static void PrintUsage()
        {
            Console.WriteLine();
            Console.WriteLine("USAGE:");
            Console.WriteLine("RandomBenchWorldGenerator.exe [-rows <int>] [-collumns <int>] [-min <int>] [-max <int>] [-out <filename>] [-help]");
            Console.WriteLine("  - rows: the number of rows in the matrix; default 32");
            Console.WriteLine("  - columns: the number of columns in the matrix; default 32");
            Console.WriteLine("  - min: the minimum range of values assigned in the cells; default 0");
            Console.WriteLine("  - max: the minimum range of values assigned in the cells; default 9");
            Console.WriteLine("  - out: the name of the file to store the matrix; default BenchWorldConfig.txt");
            Console.WriteLine("  - help: to print this help message");
            Console.WriteLine();
            Console.WriteLine("EXAMPLE:");
            Console.WriteLine("1. > RandomBenchWorldGenerator.exe ");
            Console.WriteLine("   - generates file BenchWorldConfig.txt containing 32x32 matrix with values in range [0, 9]");
            Console.WriteLine("2. > RandomBenchWorldGenerator.exe -row 100 -collumns 100 -min -10 -max 10 -out BenchWorldConfig.txt");
            Console.WriteLine("   - generates file BenchWorldConfig.txt containing 100x100 matrix with values in range [-10, 10]");
        }
    }
}
