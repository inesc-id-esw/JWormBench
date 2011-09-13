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
// File Name: Worm.cs
// Description: Abstracts a Worm configuration.
// Organization: Barcelona Supercomputing Center
// Author: Ferad Zyulkyarov ferad.zyulkyarov[@]bsc.es
// Date: 01.03.2008
//
//-----------------------------------------------------------------------------
using System;
using System.Collections.Generic;

namespace Bsc.WormBench.Tools
{
    public class Worm
    {
        #region Private fields
        //
        // Worm properties
        //
        private int id;
        private int groupId;
        private string name;
        private int headSize;
        private int speed;
        private Coordinate[] body;
        private int bodyLength;

        //
        // Initialization parameters.
        //
        private Random rand;

        #endregion

        #region Constructors
        public Worm(
            int id, 
            int groupId, 
            string name, 
            int headSize, 
            int bodyLength, 
            int speed, 
            Random rand)
        {
            this.id = id;
            this.groupId = groupId;
            this.name = name;
            this.headSize = headSize;
            this.bodyLength = bodyLength;
            if (bodyLength == 1)
            {
                this.body = new Coordinate[bodyLength + 1];
            }
            else
            {
                this.body = new Coordinate[bodyLength];
            }
            this.speed = speed;
            this.rand = rand;
        }
        #endregion

        #region Public Methods
        public override string ToString()
        {
            string wormStr = String.Format(
                "WormID = {0}; Name = {1}; GroupID = {2}; HeadSize = {3}; Speed = {4}; BodyLength = {5}; Body = {6}",
                id,
                name,
                groupId,
                headSize,
                speed,
                bodyLength,
                BodyToString());

            return wormStr;
        }

        /// <summary>
        /// Generates the body of the Worm randomly based on the
        /// rowsNum and collumnsNum.
        /// </summary>
        /// <remarks>
        /// The body generation should consider somehow the other worms in
        /// the matrix so that they don't overlap. The current implementation
        /// allows overlapping.
        /// </remarks>
        public void GenerateBody(int rowsNum, int columnsNum)
        {
            //
            // Pickup the head
            //
            body[0].x = rand.Next(0, rowsNum);
            body[0].y = rand.Next(0, columnsNum);

            //
            // Now continue to build the rest of the body.
            // The long code handles the randomness. The next cell of the worm body
            // can be in one of the four possible coordinates. But if one of these
            // possible coordinates is already occupied by another worm body it is
            // removed from the possibleOptions.
            //
            for (int i = 1; i < body.Length; i++)
            {
                Coordinate up;
                up.x = body[i - 1].x - 1;
                up.y = body[i - 1].y;

                Coordinate right;
                right.x = body[i - 1].x;
                right.y = body[i - 1].y + 1;

                Coordinate down;
                down.x = body[i - 1].x + 1;
                down.y = body[i - 1].y;

                Coordinate left;
                left.x = body[i - 1].x;
                left.y = body[i - 1].y - 1;

                List<Coordinate> possibleOptions = new List<Coordinate>(4);
                possibleOptions.Add(up);
                possibleOptions.Add(right);
                possibleOptions.Add(down);
                possibleOptions.Add(left);

                for (int j = i - 1; j > 0; j--)
                {
                    if (possibleOptions.Contains(body[j]))
                    {
                        possibleOptions.Remove(body[j]);
                    }
                }

                int randomIndex = rand.Next(possibleOptions.Count);
                body[i] = possibleOptions[randomIndex];
            }
        }
        #endregion

        #region Private Methods        

        private string BodyToString()
        {
            string strBody = string.Empty;

            for (int i = 0; i < body.Length; i++)
            {
                strBody = strBody + "[" + body[i].x + "," + body[i].y + "]";
            }

            return strBody;
        }
        #endregion

        #region Properties
        public int Id
        {
            get
            {
                return id;
            }
        }

        public int GroupId
        {
            get
            {
                return groupId;
            }
        }

        public string Name
        {
            get
            {
                return name;
            }
        }

        public int HeadSize
        {
            get
            {
                return headSize;
            }
        }

        public Coordinate[] Body
        {
            get
            {
                return body;
            }
        }
        #endregion

    }
}
