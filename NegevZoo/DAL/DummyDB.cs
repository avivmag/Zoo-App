using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
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
            Animals             = new TestDbSet<Animal>();
            Enclosures          = new TestDbSet<Enclosure>();
            WallFeeds           = new TestDbSet<WallFeed>();
            Prices              = new TestDbSet<Price>();
            GeneralInfo         = new TestDbSet<GeneralInfo>();
            RecurringEvents     = new TestDbSet<RecurringEvent>();
            SpecialEvents       = new TestDbSet<SpecialEvent>();
            OpeningHours        = new TestDbSet<OpeningHour>();
            ContactInfos        = new TestDbSet<ContactInfo>();
            Users               = new TestDbSet<User>();
            AllLanguages        = new TestDbSet<Language>();

            Animals.AddRange(InitializeAnimals());
            Enclosures.AddRange(InitializeEnclosures());
            WallFeeds.AddRange(InitializeWallFeeds());
            Prices.AddRange(InitializePrices());
            GeneralInfo.AddRange(InitializeGeneralInfo());
            RecurringEvents.AddRange(InitialRecurringEvents());
            SpecialEvents.AddRange(InitialSpecialEvents());
            OpeningHours.AddRange(InitialOpeningHour());
            ContactInfos.AddRange(InitialContactInfos());
            Users.AddRange(InitializeUsers());
            AllLanguages.AddRange(InitializeLanguages());
        }

        protected override List<TEntity> GetFromCache<TEntity>()
        {
            return null;
        }

        protected override void SetInCache<TEntity>(List<TEntity> entity)
        {
        }

        #region DbSets

        protected DbSet<Animal> Animals { get; set; }
        protected DbSet<Enclosure> Enclosures { get; set; }
        protected DbSet<RecurringEvent> RecurringEvents { get; set; }
        protected DbSet<WallFeed> WallFeeds { get; set; }
        protected DbSet<Price> Prices { get; set; }
        protected DbSet<GeneralInfo> GeneralInfo { get; set; }
        protected DbSet<OpeningHour> OpeningHours { get; set; }
        protected DbSet<ContactInfo> ContactInfos { get; set; }
        protected DbSet<SpecialEvent> SpecialEvents { get; set; }
        protected DbSet<User> Users { get; set; }
        protected DbSet<Language> AllLanguages { get; set; }

        #endregion 

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
                        id          = 1,
                        name        = "Olive Baboon",
                        enclosureId       = 1,
                        story       = "Gilor the olive baboon is very lovable.",
                        language    = (int)Languages.en
                    },

                    new Animal
                    {
                        id          = 2,
                        name        = "בבון הזית",
                        enclosureId       = 2,
                        story       = "גילאור בבון הזית מאוד חמוד",
                        language    = (int)Languages.he
                    },

                    new Animal
                    {
                        id          = 3,
                        name        = "Gorilla",
                        enclosureId       = 1,
                        story       = "Shrek the mighty gorilla!",
                        language    = (int)Languages.en
                    },

                    new Animal
                    {
                        id          = 4,
                        name        = "גורילה",
                        enclosureId       = 2,
                        story       = "שרק הוא וואחד גורילה!",
                        language    = (int)Languages.he
                    },

                    new Animal
                    {
                        id          = 5,
                        name        = "Monkey",
                        enclosureId       = 3,
                        story       = "Kofifo is Marco's monkey.",
                        language    = (int)Languages.en
                    },

                    new Animal
                    {
                        id          = 6,
                        name        = "קוף",
                        enclosureId       = 4,
                        story       = "קופיקו הוא הקוף של מרקו.",
                        language    = (int)Languages.he
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
                        id          = 1,
                        name        = "Monkeys",
                        story       = "Our monkeys have been great! They are awesome.",
                        language    = (int)Languages.en
                    },

                    new Enclosure
                    {
                        id          = 2,
                        name        = "תצוגת הקופים",
                        story       = "הקופים שלנו הם הכי הכי!",
                        language    = (int)Languages.he
                    },

                    new Enclosure
                    {
                        id          = 3,
                        name        = "Houman Monkeys",
                        story       = "Computer Science students.",
                        language    = (int)Languages.en
                    },

                    new Enclosure
                    {
                        id          = 4,
                        name        = "קופי אדם",
                        story       = "הקופים שלנו הם הכי חכמים!",
                        language    = (int)Languages.he
                    },

                    new Enclosure
                    {
                        id          = 5,
                        name        = "Zebra",
                        story       = "Our saved Zebra.",
                        language    = (int)Languages.en
                    },

                    new Enclosure
                    {
                        id          = 6,
                        name        = "זברה",
                        story       = "הזברות שלנו ניצלו משבי",
                        language    = (int)Languages.he
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
                        id          = 1,
                        info        = "Purim Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                        
                    },

                    new WallFeed
                    {
                        id          = 2,
                        info        = "אירועי פורים",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 3,
                        info        = "Passeover Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {

                        id          = 4,
                        info        = "אירועי פסח",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 5,
                        info        = "Shavuut Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 6,
                        info        = "אירועי שבועות",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 7,
                        info        = "Sukut Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 8,
                        info        = "אירועי סוכות",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },
                    new WallFeed
                    {
                        id          = 9,
                        info        = "Purim Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)

                    },

                    new WallFeed
                    {
                        id          = 10,
                        info        = "אירועי פורים",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 11,
                        info        = "Passeover Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 12,
                        info        = "אירועי פסח",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 13,
                        info        = "Shavuut Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 14,
                        info        = "אירועי שבועות",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 15,
                        info        = "Sukut Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 16,
                        info        = "אירועי סוכות",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },
                    new WallFeed
                    {
                        id          = 17,
                        info        = "Purim Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)

                    },

                    new WallFeed
                    {
                        id          = 18,
                        info        = "אירועי פורים",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 19,
                        info        = "Passeover Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 20,
                        info        = "אירועי פסח",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 21,
                        info        = "Shavuut Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 22,
                        info        = "אירועי שבועות",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 23,
                        info        = "Sukut Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 24,
                        info        = "אירועי סוכות",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },
                    new WallFeed
                    {
                        id          = 25,
                        info        = "Purim Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)

                    },

                    new WallFeed
                    {
                        id          = 26,
                        info        = "אירועי פורים",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 27,
                        info        = "Passeover Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 28,
                        info        = "אירועי פסח",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 29,
                        info        = "Shavuut Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 30,
                        info        = "אירועי שבועות",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 31,
                        info        = "Sukut Events",
                        language    = (int)Languages.en,
                        created     = new DateTime(2018,3,11)
                    },

                    new WallFeed
                    {
                        id          = 32,
                        info        = "אירועי סוכות",
                        language    = (int)Languages.he,
                        created     = new DateTime(2018,3,11)
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
                        id          = 1,
                        population  = "Adult",
                        pricePop    = 40,
                        language    = (int)Languages.en
                    },

                    new Price
                    {
                        id          = 2,
                        population  = "מבוגר",
                        pricePop    = 40,
                        language    = (int)Languages.he
                    },

                    new Price
                    {
                        id          = 3,
                        population  = "Children under 18",
                        pricePop    = 25,
                        language    = (int)Languages.en
                    },

                    new Price
                    {
                        id          = 4,
                        population  = "ילד מתחת לגיל 18",
                        pricePop    = 25,
                        language    = (int)Languages.he
                    },

                    new Price
                    {
                        id          = 5,
                        population  = "Soldier",
                        pricePop    = 25,
                        language    = (int)Languages.en
                    },

                    new Price
                    {
                        id          = 6,
                        population  = "חייל",
                        pricePop    = 25,
                        language    = (int)Languages.he
                    },

                    new Price
                    {
                        id          = 7,
                        population  = "Pensioner",
                        pricePop    = 10,
                        language    = (int)Languages.en
                    },

                    new Price
                    {
                        id          = 8,
                        population  = "פנסיונר",
                        pricePop    = 25,
                        language    = (int)Languages.he
                    },

                    new Price
                    {
                        id          = 9,
                        population  = "Student",
                        pricePop    = 10,
                        language    = (int)Languages.en
                    },

                    new Price
                    {
                        id          = 10,
                        population  = "סטודנט",
                        pricePop    = 25,
                        language    = (int)Languages.he
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
                        name                = "NegevZoo",
                        aboutUs             = "We are Negev Zoo!!! We love animals",
                        contactInfoNote     = "Contact between 08:00 - 22:00",
                        openingHoursNote    = "The cashier desk will bew closed two hours before the zoo is closing.",
                        language            = (int)Languages.en
                    },

                    new GeneralInfo
                    {
                        name                = "נגב זו",
                        aboutUs             = "אנחנו נגב זו!!! אנחנו אוהבים חיות",
                        contactInfoNote     = "ניתן ליצור קשר בין השעות 08:00 לבין 22:00",
                        openingHoursNote    = "הקופות יסגרו שעתיים לפני סגירת הגן",
                        language            = (int)Languages.he
                    }
            };
        }

        private IEnumerable<RecurringEvent> InitialRecurringEvents()
        {
            return new List<RecurringEvent>
            {
                new RecurringEvent
                {
                    id                      = 1,
                    enclosureId                   = 1,
                    day                     = "Sunday",
                    description             = "Feeding",
                    startHour               = 10,
                    startMin                = (int) AvailableMinutes.Half,
                    endHour                 = 11,
                    endMin                  =(int) AvailableMinutes.Zero,
                    language                = (int) Languages.en
                },
                new RecurringEvent
                {
                    id                      = 2,
                    enclosureId                   = 2,
                    day                     = "ראשון",
                    description             = "האכלה",
                    startHour               = 10,
                    startMin                = (int) AvailableMinutes.Half,
                    endHour                 = 11,
                    endMin                  = (int) AvailableMinutes.Zero,
                    language                = (int) Languages.he
                },
                new RecurringEvent
                {
                    id                      = 3,
                    enclosureId                   = 3,
                    day                     = "Monday",
                    description             = "Playing",
                    startHour               = 10,
                    startMin                = (int) AvailableMinutes.Half,
                    endHour                 = 11,
                    endMin                  =(int) AvailableMinutes.Zero,
                    language                = (int) Languages.en
                },
                new RecurringEvent
                {
                    id                      = 4,
                    enclosureId                   = 4,
                    day                     = "ראשון",
                    description             = "משחק",
                    startHour               = 13,
                    startMin                = (int) AvailableMinutes.Half,
                    endHour                 = 14,
                    endMin                  = (int) AvailableMinutes.Zero,
                    language                = (int) Languages.he
                },

                new RecurringEvent
                {
                    id                      = 5,
                    enclosureId                   = 3,
                    day                     = "Saturday",
                    description             = "Feeding",
                    startHour               = 10,
                    startMin                = (int) AvailableMinutes.Half,
                    endHour                 = 11,
                    endMin                  =(int) AvailableMinutes.Zero,
                    language                = (int) Languages.en
                },
                new RecurringEvent
                {
                    id                      = 6,
                    enclosureId                   = 4,
                    day                     = "שבת",
                    description             = "האכלה",
                    startHour               = 13,
                    startMin                = (int) AvailableMinutes.Half,
                    endHour                 = 14,
                    endMin                  = (int) AvailableMinutes.Zero,
                    language                = (int) Languages.he
                }
            };
        }

        private IEnumerable<SpecialEvent> InitialSpecialEvents()
        {
            return new List<SpecialEvent>
            {
                new SpecialEvent
                {
                    id                      = 1,
                    description             = "1קקי",
                    startDate               = new DateTime(2018,3,5),
                    endDate                 = new DateTime(2018,3,8),
                    language                = (int) Languages.he
                },
                new SpecialEvent
                {
                    id                      = 2,
                    description             = "Kaki1",
                    startDate               = new DateTime(2018,3,5),
                    endDate                 = new DateTime(2018,3,8),
                    language                = (int) Languages.en
                },
                new SpecialEvent
                {
                    id                      = 3,
                    description             = "אירועי פורים",
                    startDate               = new DateTime(2018,3,1),
                    endDate                 = new DateTime(2018,3,8),
                    language                = (int) Languages.he
                },
                new SpecialEvent
                {
                    id                      = 4,
                    description             = "Purim Events",
                    startDate               = new DateTime(2018,3,1),
                    endDate                 = new DateTime(2018,3,8),
                    language                = (int) Languages.en
                }
            };
        }

        private IEnumerable<OpeningHour> InitialOpeningHour()
        {
            return new List<OpeningHour>
            {
                new OpeningHour
                {
                    id = 1,
                    day = "ראשון",
                    startHour = 11,
                    startMin = (int)AvailableMinutes.Half,
                    endHour = 12,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.he
                },

                new OpeningHour
                {
                    id = 2,
                    day = "Sunday",
                    startHour = 11,
                    startMin = (int)AvailableMinutes.Half,
                    endHour = 12,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.en
                },

                new OpeningHour
                {
                    id = 3,
                    day = "שני",
                    startHour = 9,
                    startMin = (int)AvailableMinutes.Half,
                    endHour = 18,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.he
                },

                new OpeningHour
                {
                    id = 4,
                    day = "Monday",
                    startHour = 9,
                    startMin = (int)AvailableMinutes.Half,
                    endHour = 18,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.en
                },

                new OpeningHour
                {
                    id = 5,
                    day = "שלישי",
                    startHour = 9,
                    startMin = (int)AvailableMinutes.Half,
                    endHour = 18,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.he
                },

                new OpeningHour
                {
                    id = 6,
                    day = "Tuesday",
                    startHour = 9,
                    startMin = (int)AvailableMinutes.Half,
                    endHour = 18,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.en
                },

                new OpeningHour
                {
                    id = 7,
                    day = "רביעי",
                    startHour = 9,
                    startMin = (int)AvailableMinutes.Half,
                    endHour = 18,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.he
                },

                new OpeningHour
                {
                    id = 8,
                    day = "Wednesday",
                    startHour = 9,
                    startMin = (int)AvailableMinutes.Half,
                    endHour = 18,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.en
                },

                new OpeningHour
                {
                    id = 9,
                    day = "חמישי",
                    startHour = 9,
                    startMin = (int)AvailableMinutes.Quarter,
                    endHour = 18,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.he
                },

                new OpeningHour
                {
                    id = 10,
                    day = "Thursday",
                    startHour = 9,
                    startMin = (int)AvailableMinutes.Quarter,
                    endHour = 18,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.en
                },

                new OpeningHour
                {
                    id = 11,
                    day = "שבת",
                    startHour = 10,
                    startMin = (int)AvailableMinutes.ThreeQuarters,
                    endHour = 18,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.he
                },

                new OpeningHour
                {
                    id = 12,
                    day = "Saturday",
                    startHour = 9,
                    startMin = (int)AvailableMinutes.ThreeQuarters,
                    endHour = 18,
                    endMin = (int)AvailableMinutes.Zero,
                    language = (int)Languages.en
                },
            };
        }

        private IEnumerable<ContactInfo> InitialContactInfos()
        {
            return new List<ContactInfo>
            {
                new ContactInfo
                {
                    id          = 1,
                    via         = "טלפון",
                    address     = "08-641-4777",
                    language    =(int)Languages.he
                },

                new ContactInfo
                {
                    id          = 2,
                    via         = "Phone",
                    address     = "08-641-4777",
                    language    =(int)Languages.en
                },

                new ContactInfo
                {
                    id          = 3,
                    via         = "דואר",
                    address     = "דרך אילן רמון 5, באר שבע",
                    language    =(int)Languages.he
                },

                new ContactInfo
                {
                    id          = 4,
                    via         = "Post Mail",
                    address     = "Via Ilan Ramon 5, Beer-Sheva",
                    language    =(int)Languages.en
                },

                new ContactInfo
                {
                    id          = 5,
                    via         = "דואר אלקטרוני",
                    address     = "gilorisr@post.bgu.ac.il",
                    language    =(int)Languages.he
                },

                new ContactInfo
                {
                    id          = 6,
                    via         = "E-Mail",
                    address     = "gilorisr@post.bgu.ac.il",
                    language    =(int)Languages.en
                }
            };
        }

        private IEnumerable<Language> InitializeLanguages()
        {
            return new List<Language>
            {
                new Language
                {
                    id      = 1,
                    name  = "עברית"
                },
                new Language
                {
                    id      = 2,
                    name  = "English"
                },
                new Language
                {
                    id = 3,
                    name = "عربيه"
                },
                new Language
                {
                    id = 4,
                    name = "русский"
                }
            };
        }

        private IEnumerable<User> InitializeUsers()
        {
            return new List<User>
            {
                new User
                {
                    id = 1,
                    isAdmin = true,
                    name = "אור",
                    password = "123"
                },
                new User
                {
                    id = 2,
                    isAdmin = false,
                    name = "גיל",
                    password = "123"
                },
                new User
                {
                    id = 3,
                    isAdmin = true,
                    name = "מנהל",
                    password = "123"
                },
                new User
                {
                    id = 4,
                    isAdmin = false,
                    name = "עובד",
                    password = "123"
                }
            };
        }

        #endregion

        #region Getters

        public override DbSet<Animal> GetAllAnimals()
        {
            return Animals;
        }

        public override DbSet<Enclosure> GetAllEnclosures()
        {
            return Enclosures;
        }

        public override DbSet<RecurringEvent> GetAllRecuringEvents()
        {
            return RecurringEvents;
        }

        public override DbSet<Price> GetAllPrices()
        {
            return Prices;
        }

        public override DbSet<GeneralInfo> GetGeneralInfo()
        {
            return GeneralInfo;
        }

        public override DbSet<OpeningHour> GetAllOpeningHours()
        {
            return OpeningHours;
        }

        public override DbSet<ContactInfo> GetAllContactInfos()
        {
            return ContactInfos;
        }

        public override DbSet<SpecialEvent> GetAllSpecialEvents()
        {
            return SpecialEvents;
        }

        public override DbSet<WallFeed> GetAllWallFeeds()
        {
            return WallFeeds;
        }

        public override DbSet<User> GetAllUsers()
        {
            return Users;
        }

        public override DbSet<Language> GetAllLanguages()
        {
            return AllLanguages;
        }

        #endregion
    }
}
