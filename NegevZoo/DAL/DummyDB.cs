using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Backend.Models;
using Backend;
using System.Data.Entity;

namespace DAL
{
    /// <summary>
    /// Mock Database for unit testing.
    /// </summary>
    public class DummyDB : IZooDB
    {
        public DummyDB()
        {
            Animals            = new TestDbSet<Animal>();
            Enclosures         = new TestDbSet<Enclosure>();
            WallFeeds          = new TestDbSet<WallFeed>();
            Prices             = new TestDbSet<Price>();
            GeneralInfo        = new TestDbSet<GeneralInfo>();
            RecurringEvents    = new TestDbSet<RecurringEvent>();
            SpecialEvents      = new TestDbSet<SpecialEvent>();
            OpeningHours       = new TestDbSet<OpeningHour>();
            ContactInfos       = new TestDbSet<ContactInfo>();

            Animals.AddRange(InitializeAnimals());
            Enclosures.AddRange(InitializeEnclosures());
            WallFeeds.AddRange(InitializeWallFeeds());
            Prices.AddRange(InitializePrices());
            GeneralInfo.AddRange(InitializeGeneralInfo());
            RecurringEvents.AddRange(InitialRecurringEvents());
            SpecialEvents.AddRange(InitialSpecialEvents());
            OpeningHours.AddRange(InitialOpeningHour());
            ContactInfos.AddRange(InitialContactInfos());
        }

        protected override List<TEntity> GetFromCache<TEntity>()
        {
            return null;
        }

        protected override void SetInCache<TEntity>(List<TEntity> entity)
        {
        }

        #region Initializers

        /// <summary>
        /// Initializes the animals mock.
        /// </summary>
        /// <returns>Mock animals list.</returns>
        private IEnumerable<Animal> InitializeAnimals()
        {
            return new List<Animal>
                {
                    new Animal
                    {
                        Id          = 1,
                        Name        = "Olive Baboon",
                        EncId       = 1,
                        Story       = "Gilor the olive baboon is very lovable.",
                        Language    = (int)Languages.en
                    },

                    new Animal
                    {
                        Id          = 2,
                        Name        = "בבון הזית",
                        EncId       = 2,
                        Story       = "גילאור בבון הזית מאוד חמוד",
                        Language    = (int)Languages.he
                    },

                    new Animal
                    {
                        Id          = 3,
                        Name        = "Gorilla",
                        EncId       = 1,
                        Story       = "Shrek the mighty gorilla!",
                        Language    = (int)Languages.en
                    },

                    new Animal
                    {
                        Id          = 4,
                        Name        = "גורילה",
                        EncId       = 2,
                        Story       = "שרק הוא וואחד גורילה!",
                        Language    = (int)Languages.he
                    },

                    new Animal
                    {
                        Id          = 5,
                        Name        = "Monkey",
                        EncId       = 3,
                        Story       = "Kofifo is Marco's monkey.",
                        Language    = (int)Languages.en
                    },

                    new Animal
                    {
                        Id          = 6,
                        Name        = "קוף",
                        EncId       = 4,
                        Story       = "קופיקו הוא הקוף של מרקו.",
                        Language    = (int)Languages.he
                    }
                };
        }

        /// <summary>
        /// Initializes the enclosures mock.
        /// </summary>
        /// <returns>Mock enclosures list.</returns>
        private IEnumerable<Enclosure> InitializeEnclosures()
        {
            return new List<Enclosure>
                {
                    new Enclosure
                    {
                        Id          = 1,
                        Name        = "Monkeys",
                        Story       = "Our monkeys have been great! They are awesome.",
                        Language    = (int)Languages.en
                    },

                    new Enclosure
                    {
                        Id          = 2,
                        Name        = "תצוגת הקופים",
                        Story       = "הקופים שלנו הם הכי הכי!",
                        Language    = (int)Languages.he
                    },

                    new Enclosure
                    {
                        Id          = 3,
                        Name        = "Houman Monkeys",
                        Story       = "Computer Science students.",
                        Language    = (int)Languages.en
                    },

                    new Enclosure
                    {
                        Id          = 4,
                        Name        = "קופי אדם",
                        Story       = "הקופים שלנו הם הכי חכמים!",
                        Language    = (int)Languages.he
                    },

                    new Enclosure
                    {
                        Id          = 5,
                        Name        = "Zebra",
                        Story       = "Our saved Zebra.",
                        Language    = (int)Languages.en
                    },

                    new Enclosure
                    {
                        Id          = 6,
                        Name        = "זברה",
                        Story       = "הזברות שלנו ניצלו משבי",
                        Language    = (int)Languages.he
                    }
                };
        }

        /// <summary>
        /// Initializes the wall feeds mock.
        /// </summary>
        /// <returns>Mock wall feeds list.</returns>
        private IEnumerable<WallFeed> InitializeWallFeeds()
        {
            return new List<WallFeed>
                {
                    new WallFeed
                    {
                        Id          = 1,
                        Info        = "Purim Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                        
                    },

                    new WallFeed
                    {
                        Id          = 2,
                        Info        = "אירועי פורים",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 3,
                        Info        = "Passeover Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {

                        Id          = 4,
                        Info        = "אירועי פסח",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 5,
                        Info        = "Shavuut Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 6,
                        Info        = "אירועי שבועות",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 7,
                        Info        = "Sukut Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 8,
                        Info        = "אירועי סוכות",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },
                    new WallFeed
                    {
                        Id          = 9,
                        Info        = "Purim Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)

                    },

                    new WallFeed
                    {
                        Id          = 10,
                        Info        = "אירועי פורים",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 11,
                        Info        = "Passeover Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 12,
                        Info        = "אירועי פסח",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 13,
                        Info        = "Shavuut Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 14,
                        Info        = "אירועי שבועות",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 15,
                        Info        = "Sukut Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 16,
                        Info        = "אירועי סוכות",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },
                    new WallFeed
                    {
                        Id          = 17,
                        Info        = "Purim Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)

                    },

                    new WallFeed
                    {
                        Id          = 18,
                        Info        = "אירועי פורים",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 19,
                        Info        = "Passeover Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 20,
                        Info        = "אירועי פסח",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 21,
                        Info        = "Shavuut Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 22,
                        Info        = "אירועי שבועות",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 23,
                        Info        = "Sukut Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 24,
                        Info        = "אירועי סוכות",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },
                    new WallFeed
                    {
                        Id          = 25,
                        Info        = "Purim Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)

                    },

                    new WallFeed
                    {
                        Id          = 26,
                        Info        = "אירועי פורים",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 27,
                        Info        = "Passeover Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 28,
                        Info        = "אירועי פסח",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 29,
                        Info        = "Shavuut Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 30,
                        Info        = "אירועי שבועות",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 31,
                        Info        = "Sukut Events",
                        Language    = (int)Languages.en,
                        Created     = new DateTime(18,3,11)
                    },

                    new WallFeed
                    {
                        Id          = 32,
                        Info        = "אירועי סוכות",
                        Language    = (int)Languages.he,
                        Created     = new DateTime(18,3,11)
                    }
                };
        }

        /// <summary>
        /// Initializes the prices mock.
        /// </summary>
        /// <returns>Mock prices list.</returns>
        private IEnumerable<Price> InitializePrices()
        {
            return new List<Price>
                {
                    new Price
                    {
                        Id          = 1,
                        Population  = "Adult",
                        PricePop    = 40,
                        Language    = (int)Languages.en
                    },

                    new Price
                    {
                        Id          = 2,
                        Population  = "מבוגר",
                        PricePop    = 40,
                        Language    = (int)Languages.he
                    },

                    new Price
                    {
                        Id          = 3,
                        Population  = "Children under 18",
                        PricePop    = 25,
                        Language    = (int)Languages.en
                    },

                    new Price
                    {
                        Id          = 4,
                        Population  = "ילד מתחת לגיל 18",
                        PricePop    = 25,
                        Language    = (int)Languages.he
                    },

                    new Price
                    {
                        Id          = 5,
                        Population  = "Soldier",
                        PricePop    = 25,
                        Language    = (int)Languages.en
                    },

                    new Price
                    {
                        Id          = 6,
                        Population  = "חייל",
                        PricePop    = 25,
                        Language    = (int)Languages.he
                    },

                    new Price
                    {
                        Id          = 7,
                        Population  = "Pensioner",
                        PricePop    = 10,
                        Language    = (int)Languages.en
                    },

                    new Price
                    {
                        Id          = 8,
                        Population  = "פנסיונר",
                        PricePop    = 25,
                        Language    = (int)Languages.he
                    },

                    new Price
                    {
                        Id          = 9,
                        Population  = "Student",
                        PricePop    = 10,
                        Language    = (int)Languages.en
                    },

                    new Price
                    {
                        Id          = 10,
                        Population  = "סטודנט",
                        PricePop    = 25,
                        Language    = (int)Languages.he
                    }
            };
        }

        /// <summary>
        /// Initializes the General Info mock.
        /// </summary>
        /// <returns>Mock general info list.</returns>
        private IEnumerable<GeneralInfo> InitializeGeneralInfo()
        {
            return new List<GeneralInfo>
                {
                    new GeneralInfo
                    {
                        Name                = "NegevZoo",
                        aboutUs             = "We are Negev Zoo!!! We love animals",
                        contactInfoNote     = "Contact between 08:00 - 22:00",
                        openingHoursNote    = "The cashier desk will bew closed two hours before the zoo is closing.",
                        Language            = (int)Languages.en
                    },

                    new GeneralInfo
                    {
                        Name                = "נגב זו",
                        aboutUs             = "אנחנו נגב זו!!! אנחנו אוהבים חיות",
                        contactInfoNote     = "ניתן ליצור קשר בין השעות 08:00 לבין 22:00",
                        openingHoursNote    = "הקופות יסגרו שעתיים לפני סגירת הגן",
                        Language            = (int)Languages.he
                    }
            };
        }

        private IEnumerable<RecurringEvent> InitialRecurringEvents()
        {
            return new List<RecurringEvent>
            {
                new RecurringEvent
                {
                    Id                      = 1,
                    EncId                   = 1,
                    Day                     = "Sunday",
                    Descroption             = "Feeding",
                    StartHour               = 10,
                    StartMin                = (int) AvailableMinutes.Half,
                    EndHour                 = 11,
                    EndMin                  =(int) AvailableMinutes.Zero,
                    Language                = (int) Languages.en
                },
                new RecurringEvent
                {
                    Id                      = 2,
                    EncId                   = 2,
                    Day                     = "ראשון",
                    Descroption             = "האכלה",
                    StartHour               = 10,
                    StartMin                = (int) AvailableMinutes.Half,
                    EndHour                 = 11,
                    EndMin                  = (int) AvailableMinutes.Zero,
                    Language                = (int) Languages.he
                },
                new RecurringEvent
                {
                    Id                      = 3,
                    EncId                   = 3,
                    Day                     = "Monday",
                    Descroption             = "Playing",
                    StartHour               = 10,
                    StartMin                = (int) AvailableMinutes.Half,
                    EndHour                 = 11,
                    EndMin                  =(int) AvailableMinutes.Zero,
                    Language                = (int) Languages.en
                },
                new RecurringEvent
                {
                    Id                      = 4,
                    EncId                   = 4,
                    Day                     = "ראשון",
                    Descroption             = "משחק",
                    StartHour               = 13,
                    StartMin                = (int) AvailableMinutes.Half,
                    EndHour                 = 14,
                    EndMin                  = (int) AvailableMinutes.Zero,
                    Language                = (int) Languages.he
                },

                new RecurringEvent
                {
                    Id                      = 5,
                    EncId                   = 3,
                    Day                     = "Saturday",
                    Descroption             = "Feeding",
                    StartHour               = 10,
                    StartMin                = (int) AvailableMinutes.Half,
                    EndHour                 = 11,
                    EndMin                  =(int) AvailableMinutes.Zero,
                    Language                = (int) Languages.en
                },
                new RecurringEvent
                {
                    Id                      = 6,
                    EncId                   = 4,
                    Day                     = "שבת",
                    Descroption             = "האכלה",
                    StartHour               = 13,
                    StartMin                = (int) AvailableMinutes.Half,
                    EndHour                 = 14,
                    EndMin                  = (int) AvailableMinutes.Zero,
                    Language                = (int) Languages.he
                }
            };
        }

        private IEnumerable<SpecialEvent> InitialSpecialEvents()
        {
            return new List<SpecialEvent>
            {
                new SpecialEvent
                {
                    Id                      = 1,
                    Description             = "1קקי",
                    StartDate               = new DateTime(2018,3,5),
                    EndDate                 = new DateTime(2018,3,8),
                    Language                = (int) Languages.he
                },
                new SpecialEvent
                {
                    Id                      = 2,
                    Description             = "Kaki1",
                    StartDate               = new DateTime(2018,3,5),
                    EndDate                 = new DateTime(2018,3,8),
                    Language                = (int) Languages.en
                },
                new SpecialEvent
                {
                    Id                      = 3,
                    Description             = "אירועי פורים",
                    StartDate               = new DateTime(2018,3,1),
                    EndDate                 = new DateTime(2018,3,8),
                    Language                = (int) Languages.he
                },
                new SpecialEvent
                {
                    Id                      = 4,
                    Description             = "Purim Events",
                    StartDate               = new DateTime(2018,3,1),
                    EndDate                 = new DateTime(2018,3,8),
                    Language                = (int) Languages.en
                }
            };
        }

        private IEnumerable<OpeningHour> InitialOpeningHour()
        {
            return new List<OpeningHour>
            {
                new OpeningHour
                {
                    Id = 1,
                    Day = "ראשון",
                    StartHour = 11,
                    StartMin = (int)AvailableMinutes.Half,
                    EndHour = 12,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.he
                },

                new OpeningHour
                {
                    Id = 2,
                    Day = "Sunday",
                    StartHour = 11,
                    StartMin = (int)AvailableMinutes.Half,
                    EndHour = 12,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.en
                },

                new OpeningHour
                {
                    Id = 3,
                    Day = "שני",
                    StartHour = 9,
                    StartMin = (int)AvailableMinutes.Half,
                    EndHour = 18,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.he
                },

                new OpeningHour
                {
                    Id = 4,
                    Day = "Monday",
                    StartHour = 9,
                    StartMin = (int)AvailableMinutes.Half,
                    EndHour = 18,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.en
                },

                new OpeningHour
                {
                    Id = 5,
                    Day = "שלישי",
                    StartHour = 9,
                    StartMin = (int)AvailableMinutes.Half,
                    EndHour = 18,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.he
                },

                new OpeningHour
                {
                    Id = 6,
                    Day = "Tuesday",
                    StartHour = 9,
                    StartMin = (int)AvailableMinutes.Half,
                    EndHour = 18,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.en
                },

                new OpeningHour
                {
                    Id = 7,
                    Day = "רביעי",
                    StartHour = 9,
                    StartMin = (int)AvailableMinutes.Half,
                    EndHour = 18,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.he
                },

                new OpeningHour
                {
                    Id = 8,
                    Day = "Wednesday",
                    StartHour = 9,
                    StartMin = (int)AvailableMinutes.Half,
                    EndHour = 18,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.en
                },

                new OpeningHour
                {
                    Id = 9,
                    Day = "חמישי",
                    StartHour = 9,
                    StartMin = (int)AvailableMinutes.Quarter,
                    EndHour = 18,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.he
                },

                new OpeningHour
                {
                    Id = 10,
                    Day = "Thursday",
                    StartHour = 9,
                    StartMin = (int)AvailableMinutes.Quarter,
                    EndHour = 18,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.en
                },

                new OpeningHour
                {
                    Id = 11,
                    Day = "שבת",
                    StartHour = 10,
                    StartMin = (int)AvailableMinutes.ThreeQuarters,
                    EndHour = 18,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.he
                },

                new OpeningHour
                {
                    Id = 12,
                    Day = "Saturday",
                    StartHour = 9,
                    StartMin = (int)AvailableMinutes.ThreeQuarters,
                    EndHour = 18,
                    EndMin = (int)AvailableMinutes.Zero,
                    Language = (int)Languages.en
                },
            };
        }

        private IEnumerable<ContactInfo> InitialContactInfos()
        {
            return new List<ContactInfo>
            {
                new ContactInfo
                {
                    Id          = 1,
                    Via         = "טלפון",
                    Address     = "08-641-4777",
                    Language    =(int)Languages.he
                },

                new ContactInfo
                {
                    Id          = 2,
                    Via         = "Phone",
                    Address     = "08-641-4777",
                    Language    =(int)Languages.en
                },

                new ContactInfo
                {
                    Id          = 3,
                    Via         = "דואר",
                    Address     = "דרך אילן רמון 5, באר שבע",
                    Language    =(int)Languages.he
                },

                new ContactInfo
                {
                    Id          = 4,
                    Via         = "Post Mail",
                    Address     = "Via Ilan Ramon 5, Beer-Sheva",
                    Language    =(int)Languages.en
                },

                new ContactInfo
                {
                    Id          = 5,
                    Via         = "דואר אלקטרוני",
                    Address     = "gilorisr@post.bgu.ac.il",
                    Language    =(int)Languages.he
                },

                new ContactInfo
                {
                    Id          = 6,
                    Via         = "E-Mail",
                    Address     = "gilorisr@post.bgu.ac.il",
                    Language    =(int)Languages.en
                }
            };
        }
        #endregion
    }
}
