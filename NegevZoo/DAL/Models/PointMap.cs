using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Models
{
    public class PointMap
    {
        public int Left { get; set; }
        public int Right { get; set; }

        public PointMap(int left, int right)
        {
            Left    = left;
            Right   = right;
        }

        public override bool Equals(object obj)
        {
            if (obj.GetType() != typeof(PointMap))
            {
                return false;
            }

            PointMap other = (PointMap)obj;
            return other.Left == Left && other.Right == Right;
        }

        public override int GetHashCode()
        {
            int hash = 17;

            hash = hash * 23 + Left;
            hash = hash * 23 + Right;
            return hash;
        }
    }
}
