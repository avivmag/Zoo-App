using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Backend
{
    public enum Languages
    {
        he = 1,
        en = 2,
        ar = 3
    };

    public enum AvailableMinutes
    {
        Zero            = 0,
        Quarter         = 15,
        Half            = 30,
        ThreeQuarters   = 45
    };

    public enum Preservation
    {
        LeastConcern            = 1,
        NearThreatened          = 2,
        Vulnerable              = 3,
        Endangered              = 4,
        CriticallyEndangered    = 5,
        ExtinctWildlife         = 6
    }
}
