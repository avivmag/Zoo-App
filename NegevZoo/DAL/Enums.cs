using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL
{
    public enum Languages
    {
        he = 1,
        en = 2,
        ar = 3,
        ru = 4
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
        ExtinctWildlife         = 6,
        Unknown                 = 7
    }

    public enum Days
    {
        ראשון = 1,
        שני = 2,
        שלישי = 3,
        רביעי = 4,
        חמישי = 5,
        שישי = 6,
         שבת = 7,

        Sunday = 11,
         Monday = 12,
         Tuesday = 13,
         Wednesday = 14,
         Thursday = 15,
         Friday = 16,
         Saturday = 17,

        لأحد = 21,
        الاثنين‬ = 22,
        الثلاثا = 23,
        الأربعا‬ = 24,
        الخميس‬ = 25,
        الجمعة‬ = 26,
        السبت‬ = 27,

        Воскресенье = 31,
        Понедельник = 32,
        Вторник = 33,
        Среда = 34,
        Четверг = 35,
        Пятница = 36,
        Суббота = 37
    }
}
