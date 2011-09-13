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
// File Name: WormConfigGenerator.cs
// Description: Generates a Worm configuration file.
// Organization: Barcelona Supercomputing Center
// Author: Ferad Zyulkyarov ferad.zyulkyarov[@]bsc.es
// Date: 04.02.2008
//
//-----------------------------------------------------------------------------

using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace Bsc.WormBench.Tools
{
    public struct Coordinate
    {
        public int x;
        public int y;

        public override bool Equals(object obj)
        {
            return ((Coordinate)obj).x == this.x && ((Coordinate)obj).y == this.y;
        }

        public override int GetHashCode()
        {
            return base.GetHashCode();
        }
    }

    public class WormConfigGenerator
    {
        private int num;
        private int headSizeMin;
        private int headSizeMax;
        private int bodyLengthMin;
        private int bodyLengthMax;
        private int speed;
        private int rowsNum;
        private int columnsNum;        

        private Random rand;

        private Worm[] worms;

        public WormConfigGenerator(
            int num,
            int headSizeMin,
            int headSizeMax,
            int bodyLengthMin,
            int bodyLengthMax,
            int speed,           
            int matrixRowsNum,
            int matrixColumnsNum)
        {
            this.num = num;            
            this.headSizeMin = headSizeMin;
            this.headSizeMax = headSizeMax;
            this.bodyLengthMin = bodyLengthMin;
            this.bodyLengthMax = bodyLengthMax;
            this.speed = speed;
            this.rowsNum = matrixRowsNum;
            this.columnsNum = matrixColumnsNum;

            rand = new Random((int)DateTime.Now.Ticks);

            InitializeWorms();            
        }

        public void GenerateWorms()
        {
            int maxRowsBound = 0;

            //
            // If rowsNum is not specified (is zero) create configuration files for
            // for the range of 16-1024 rows.
            //
            if (rowsNum == 0)
            {
                rowsNum = 16;
                columnsNum = 16;
                maxRowsBound = 1024;
            }
            else
            {
                maxRowsBound = rowsNum;
            }

            //
            // If rowsNum is 0 will create files for rows 16-1024
            //
            int rowsCount = rowsNum;
            int columnsCount = columnsNum;

            do
            {
                for (int j = 0; j < worms.Length; j++)
                {
                    worms[j].GenerateBody(rowsCount, rowsCount);
                }

                string fileName = GetFileName(rowsCount);

                Save(fileName);

                Console.WriteLine("File '{0}' created.", fileName);

                rowsCount *= 2;
                columnsCount *= 2;

            } while (rowsCount <= maxRowsBound);
        }

        private string GetFileName(int rowsCount)
        {
            string fileName = string.Format(
                   "W-B[{0}.{1}]-H[{2}.{3}]-{4}.txt",                   
                   bodyLengthMin,                   
                   bodyLengthMax,
                   headSizeMin,
                   headSizeMax,
                   rowsCount);
            return fileName;
        }

        private void InitializeWorms()
        {
            worms = new Worm[this.num];

            for (int i = 0; i < worms.Length; i++)
            {
                int headSize = rand.Next(headSizeMin, headSizeMax + 1);
                int bodyLength = rand.Next(bodyLengthMin, bodyLengthMax + 1);

                worms[i] = new Worm(
                    i + 1,          // Shouldn't be zero
                    i + 1,
                    "Worm" + (i + 1),
                    headSize,
                    bodyLength,
                    speed,
                    rand);               
            }
        }        

        private void Save(string fileName)
        {           
            using (StreamWriter sw = new StreamWriter(fileName))
            {
                sw.WriteLine("WormsCount = " + worms.Length);

                foreach (Worm w in worms)
                {
                    sw.WriteLine(w.ToString());
                }
            }
        }
    }
}
