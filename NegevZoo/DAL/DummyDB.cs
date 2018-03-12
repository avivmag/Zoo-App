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

            Animals.AddRange(InitializeAnimals());
            Enclosures.AddRange(InitializeEnclosures());
            WallFeeds.AddRange(InitializeWallFeeds());
            WallFeeds.AddRange(InitializeWallFeeds());
            WallFeeds.AddRange(InitializeWallFeeds());
            WallFeeds.AddRange(InitializeWallFeeds());
            WallFeeds.AddRange(InitializeWallFeeds());
            WallFeeds.AddRange(InitializeWallFeeds());
            WallFeeds.AddRange(InitializeWallFeeds());
            WallFeeds.AddRange(InitializeWallFeeds());
            Prices.AddRange(InitializePrices());
            GeneralInfo.AddRange(InitializeGeneralInfo());
            RecurringEvents.AddRange(InitialRecurringEvents());
            SpecialEvents.AddRange(InitialSpecialEvents());
            OpeningHours.AddRange(InitialOpeningHour());
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
                        Stories        = "Purim Events",
                        Language    = (int)Languages.en
                    },

                    new WallFeed
                    {
                        Id          = 1,
                        Stories        = "אירועי פורים",
                        Language    = (int)Languages.he
                    },

                    new WallFeed
                    {
                        Id          = 2,
                        Stories        = "Passeover Events",
                        Language    = (int)Languages.en
                    },

                    new WallFeed
                    {
                        Id          = 2,
                        Stories        = "אירועי פסח",
                        Language    = (int)Languages.he
                    },

                    new WallFeed
                    {
                        Id          = 3,
                        Stories        = "Shavuut Events",
                        Language    = (int)Languages.en
                    },

                    new WallFeed
                    {
                        Id          = 3,
                        Stories        = "אירועי שבועות",
                        Language    = (int)Languages.he
                    },

                    new WallFeed
                    {
                        Id          = 4,
                        Stories        = "Sukut Events",
                        Language    = (int)Languages.en
                    },

                    new WallFeed
                    {
                        Id          = 4,
                        Stories        = "אירועי סוכות",
                        Language    = (int)Languages.he
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
                        Id          = 1,
                        Population  = "מבוגר",
                        PricePop    = 40,
                        Language    = (int)Languages.he
                    },

                    new Price
                    {
                        Id          = 2,
                        Population  = "Children under 18",
                        PricePop    = 25,
                        Language    = (int)Languages.en
                    },

                    new Price
                    {
                        Id          = 2,
                        Population  = "ילד מתחת לגיל 18",
                        PricePop    = 25,
                        Language    = (int)Languages.he
                    },

                    new Price
                    {
                        Id          = 3,
                        Population  = "Soldier",
                        PricePop    = 25,
                        Language    = (int)Languages.en
                    },

                    new Price
                    {
                        Id          = 3,
                        Population  = "חייל",
                        PricePop    = 25,
                        Language    = (int)Languages.he
                    },

                    new Price
                    {
                        Id          = 4,
                        Population  = "Pensioner",
                        PricePop    = 10,
                        Language    = (int)Languages.en
                    },

                    new Price
                    {
                        Id          = 4,
                        Population  = "פנסיונר",
                        PricePop    = 25,
                        Language    = (int)Languages.he
                    },

                    new Price
                    {
                        Id          = 5,
                        Population  = "Student",
                        PricePop    = 10,
                        Language    = (int)Languages.en
                    },

                    new Price
                    {
                        Id          = 5,
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
                        openingHoursNote    = "This is the opening hours note. todo: fill",
                        Language            = (int)Languages.en
                    },

                    new GeneralInfo
                    {
                        Name                = "נגב זו",
                        aboutUs             = "אנחנו נגב זו!!! אנחנו אוהבים חיות",
                        contactInfoNote     = "ניתן ליצור קשר בין השעות 08:00 לבין 22:00",
                        openingHoursNote    = "משהו על שעות פתיחה",
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
                    StartHour               = 10.30,
                    EndHour                 = 11.00,
                    Language                = (int) Languages.en
                },
                new RecurringEvent
                {
                    Id                      = 2,
                    EncId                   = 2,
                    Day                     = "ראשון",
                    Descroption             = "האכלה",
                    StartHour               = 10.30,
                    EndHour                 = 11.00,
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
                    Description             = "Kaki",
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
        #endregion
    }
}
