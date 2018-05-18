using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Models
{
    public class PointMap
    {
        public int X { get; set; }
        public int Y { get; set; }

        public PointMap(int x, int y)
        {
            X    = x;
            Y    = y;
        }

        public override bool Equals(object obj)
        {
            if (obj.GetType() != typeof(PointMap))
            {
                return false;
            }

            PointMap other = (PointMap)obj;
            return other.X == X && other.Y == Y;
        }

        public override int GetHashCode()
        {
            int hash = 17;

            hash = hash * 23 + X;
            hash = hash * 23 + Y;
            return hash;
        }
    }
}
