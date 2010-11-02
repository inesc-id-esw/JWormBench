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
// File Name: OperationsGeneratorHandler.cs
// Description: The handler class that actually generates the operations.
// Organization: Barcelona Supercomputing Center
// Author: Ferad Zyulkyarov ferad.zyulkyarov[@]bsc.es
// Date: 26.02.2008
//
//-----------------------------------------------------------------------------

using System;
using System.IO;
using System.Collections.Generic;

namespace Bsc.WormBench.Tools {
  /// <summary>
  /// The handler class that actually generates the operations.
  /// </summary>
  public class OperationsGeneratorHandler {
    private int operationsNum;
    private List<int> operationTypes;
    private string outFile;
    private Random rand;

    public OperationsGeneratorHandler(
        int operationsNum,
        string operationTypes,
        string operationsRatio,
        string outFile) {
      this.operationsNum = operationsNum;
      this.outFile = outFile;

      //
      // Parse the given operation types
      // They are expected to be space separated integers.
      // Example: "1 2 3 4"
      //
      this.operationTypes = new List<int>(20);
      string[] tokens = operationTypes.Split(' ');
      string[] opsRatios = operationsRatio.Split(' '); // added by FMC

      for (int i = 0; i < tokens.Length; i++) {
        tokens[i] = tokens[i].Trim();
        if (tokens[i].Length > 0) {
          int ratio = Int32.Parse(opsRatios[i]);
          for (int j = 0; j < ratio; j++) {
            this.operationTypes.Add(Int32.Parse(tokens[i])); 
          }
        }
      }

      this.rand = new Random((int)DateTime.Now.Ticks);
    }

    /// <summary>
    /// Generate the operations with random distribution.
    /// </summary>
    public void GenerateRandom() {
      using (StreamWriter sw = new StreamWriter(outFile)) {
        string line = String.Format("{0}", operationsNum);
        sw.WriteLine(line);

        for (int i = 0; i < operationsNum; i++) {
          int opTypeIndex = rand.Next(operationTypes.Count);
          int direction = rand.Next(3);

          line = String.Format("{0} - {1}", operationTypes[opTypeIndex], direction);

          sw.WriteLine(line);
        }
      }
    }
  }
}
