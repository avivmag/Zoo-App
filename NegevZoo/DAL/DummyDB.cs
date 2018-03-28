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
        private static IZooDB zdb;

        public static IZooDB GetInstance()
        {
            zdb = zdb ?? new DummyDB();

            return zdb;
        }

        private DummyDB()
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
            AllEnclosursDetails = new TestDbSet<EnclosureDetail>();
            AllAnimalsDetails   = new TestDbSet<AnimalDetail>();
            AllEnclosurePictures= new TestDbSet<EnclosurePicture>();
            AllYoutubeVideoUrls = new TestDbSet<YoutubeVideoUrl>();

            AllLanguages.AddRange(InitializeLanguages());
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
            AllEnclosursDetails.AddRange(InitializeEnclosureDetails());
            AllEnclosurePictures.AddRange(InitializeEnclosurePictures());
            AllYoutubeVideoUrls.AddRange(InitializeYouTubeVidoes());
            AllAnimalsDetails.AddRange(InitializeAnimalsDetails());
        }

        private IZooDB CreateInstance()
        {
            throw new NotImplementedException();
        }

        public static void CleanDb(bool isTesting = true)
        {
            if (isTesting)
            {
                zdb = null;
            }
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
        protected DbSet<EnclosureDetail> AllEnclosursDetails { get; set; }
        protected DbSet<AnimalDetail> AllAnimalsDetails { get; set; }
        protected DbSet<EnclosurePicture> AllEnclosurePictures { get; set; }
        protected DbSet<YoutubeVideoUrl> AllYoutubeVideoUrls { get; set; }
        protected DbSet<Devices> AllDevices { get; set; }

        #endregion 

        #region Initializers
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
                        name        = "תצוגת הקופים",
                    },

                    new Enclosure
                    {
                        id          = 2,
                        name        = "קופי אדם",
                    },

                    new Enclosure
                    {
                        id          = 3,
                        name        = "זברה",
                    },

                    new Enclosure
                    {
                        id          = 4,
                        name        = "קרנף לבן"
                    }
            };
        }

        private IEnumerable<EnclosureDetail> InitializeEnclosureDetails()
        {
            return new List<EnclosureDetail>
            {
                //Monkeys
                new EnclosureDetail
                {
                    encId = 1,
                    language = GetAllLanguages().SingleOrDefault(l => l.id == 1).id, //hebrew
                    name = "תצוגת הקופים",
                    story = "הקופים שלנו הם הכי הכי!"

                },
                new EnclosureDetail
                {
                    encId = 1,
                    language = GetAllLanguages().SingleOrDefault(l => l.id == 2).id, //english
                    name = "Monkeys",
                    story = "Our monkeys have been great! They are awesome."
                },

                //houman monkys
                new EnclosureDetail
                {
                    encId = 2,
                    language = GetAllLanguages().SingleOrDefault(l => l.id == 1).id, //hebrew
                    name = "קופי אדם",
                    story = "הקופים שלנו הם הכי חכמים!"

                },
                new EnclosureDetail
                {
                    encId = 2,
                    language = GetAllLanguages().SingleOrDefault(l => l.id == 2).id, //english
                    name = "Houman Monkeys",
                    story = "Computer Science students."

                },

                //zebra
                new EnclosureDetail
                {
                    encId = 3,
                    language = GetAllLanguages().SingleOrDefault(l => l.id == 1).id, //hebrew
                    name = "זברה",
                    story = "הזברות שלנו ניצלו משבי"

                },
                new EnclosureDetail
                {
                    encId = 3,
                    language = GetAllLanguages().SingleOrDefault(l => l.id == 2).id, //english
                    name = "Zebra",
                    story = "Our saved Zebra."

                },

                new EnclosureDetail
                {
                    encId = 4,
                    language = GetAllLanguages().SingleOrDefault(l => l.id == 1).id, //hebrew
                    name = "קרנף לבן",
                    story = "למרות שכמעט ונכחד בטבע, בשבי עדיין קיימים מספר פריטים"
                }
            };
        }

        private IEnumerable<EnclosurePicture> InitializeEnclosurePictures()
        {
            return new List<EnclosurePicture>
            {
                new EnclosurePicture
                {
                    id = 1,
                    enclosureId = 1,
                    pictureUrl = "url1"
                },

                new EnclosurePicture
                {
                    id = 2,
                    enclosureId = 2,
                    pictureUrl = "url2"
                },
                new EnclosurePicture
                {
                    id = 3,
                    enclosureId = 2,
                    pictureUrl = "url3"
                }
            };
        }

        private IEnumerable<YoutubeVideoUrl> InitializeYouTubeVidoes()
        {
            return new List<YoutubeVideoUrl>
            {
                new YoutubeVideoUrl
                {
                    id = 1,
                    enclosureId = 1,
                    videoUrl = "video1"
                },
                new YoutubeVideoUrl
                {
                    id = 2,
                    enclosureId = 2,
                    videoUrl = "video2"
                },
                new YoutubeVideoUrl
                {
                    id = 3,
                    enclosureId = 2,
                    videoUrl = "video3"
                }
            };
        }

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
                        name        = "בבון הזית",
                        enclosureId = 1
                    },

                    new Animal
                    {
                        id          = 2,
                        name        = "גורילה",
                        enclosureId = 1,
                    },

                    new Animal
                    {
                        id          = 3,
                        name        = "קוף",
                        enclosureId = 2
                    }
                };
        }

        /// <summary>
        /// Initializes the animals details mock.
        /// </summary>
        /// <returns>Mock animal detailslist.</returns>
        private IEnumerable<AnimalDetail> InitializeAnimalsDetails()
        {
            return new List<AnimalDetail>
            {
                //baboon
                new AnimalDetail
                {
                    animalId        = 1,
                    language        = AllLanguages.SingleOrDefault(l => l.id == 1).id, //hebrew
                    name            = "בבון הזית",
                    category        = "קופים",
                    series          = "קוף",
                    distribution    = "",
                    family          = "",
                    food            = "",
                    reproduction    = "",
                    story           = "גילאור בבון הזית מאוד חמוד"
                },
                new AnimalDetail
                {
                    animalId        = 1,
                    language        = AllLanguages.SingleOrDefault(l => l.id == 2).id, //english
                    name            = "Olive Baboon",
                    category        = "Monkies",
                    series          = "Monk",
                    distribution    = "",
                    family          = "",
                    food            = "",
                    reproduction    = "",
                    story           = "Gilor the olive baboon is very lovable."
                },

                //Gorila
                new AnimalDetail
                {
                    animalId        = 2,
                    language        = AllLanguages.SingleOrDefault(l => l.id == 1).id, //hebrew
                    name            = "גורילה",
                    category        = "קופים",
                    series          = "קוף",
                    distribution    = "",
                    family          = "",
                    food            = "",
                    reproduction    = "",
                    story           = "שרק הוא וואחד גורילה!"
                },
                new AnimalDetail
                {
                    animalId        = 2,
                    language        = AllLanguages.SingleOrDefault(l => l.id == 2).id, //english
                    name            = "Gorila",
                    category        = "Monkies",
                    series          = "Monk",
                    distribution    = "",
                    family          = "",
                    food            = "",
                    reproduction    = "",
                    story           = "Shrek the mighty gorilla!"
                },

                //Monkey
                new AnimalDetail
                {
                    animalId        = 3,
                    language        = AllLanguages.SingleOrDefault(l => l.id == 1).id, //hebrew
                    name            = "קוף",
                    category        = "קופים",
                    series          = "קוף",
                    distribution    = "",
                    family          = "",
                    food            = "",
                    reproduction    = "",
                    story           = "קופיקו הוא הקוף של מרקו."
                },
                new AnimalDetail
                {
                    animalId        = 3,
                    language        = AllLanguages.SingleOrDefault(l => l.id == 2).id, //english
                    name            = "Monkey",
                    category        = "Monkies",
                    series          = "Monk",
                    distribution    = "",
                    family          = "",
                    food            = "",
                    reproduction    = "",
                    story           = "Kofifo is Marco's monkey."
                },
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
                    enclosureId             = 1,
                    day                     = 1,
                    description             = "האכלה",
                    startTime               = new TimeSpan(10, 30, 0),
                    endTime                 = new TimeSpan(11, 0, 0),
                    language                = GetAllLanguages().SingleOrDefault(l => l.name == "עברית").id
                },
                new RecurringEvent
                {
                    id                      = 2,
                    enclosureId             = 1,
                    day                     = 11,
                    description             = "Feeding",
                    startTime               = new TimeSpan(10, 30, 0),
                    endTime                 = new TimeSpan(11, 0, 0),
                    language                = GetAllLanguages().SingleOrDefault(l => l.name == "English").id
                },

                new RecurringEvent
                {
                    id                      = 3,
                    enclosureId             = 2,
                    day                     = 1,
                    description             = "משחק",
                    startTime               = new TimeSpan(13, 30, 0),
                    endTime                 = new TimeSpan(14, 0, 0),
                    language                = GetAllLanguages().SingleOrDefault(l => l.name == "עברית").id
                },
                new RecurringEvent
                {
                    id                      = 4,
                    enclosureId             = 2,
                    day                     = 1,
                    description             = "Playing",
                    startTime               = new TimeSpan(13, 30, 0),
                    endTime                 = new TimeSpan(14, 0, 0),
                    language                = GetAllLanguages().SingleOrDefault(l => l.name == "English").id
                },
                
                new RecurringEvent
                {
                    id                      = 5,
                    enclosureId             = 2,
                    day                     = 7,
                    description             = "האכלה",
                    startTime               = new TimeSpan(10, 30, 0),
                    endTime                 = new TimeSpan(11, 0, 0),
                    language                = GetAllLanguages().SingleOrDefault(l => l.name == "עברית").id
                },
                new RecurringEvent
                {
                    id                      = 6,
                    enclosureId             = 2,
                    day                     = 17,
                    description             = "Feeding",
                    startTime               = new TimeSpan(10, 30, 0),
                    endTime                 = new TimeSpan(11, 0, 0),
                    language                = GetAllLanguages().SingleOrDefault(l => l.name == "English").id
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
                    id          = 1,
                    day         = 1,
                    startTime   = new TimeSpan(11, 30, 00),
                    endTime     = new TimeSpan(12, 0, 0),
                    language    = GetAllLanguages().SingleOrDefault(l => l.name == "עברית").id
                },

                new OpeningHour
                {
                    id          = 2,
                    day         = 11,
                    startTime   = new TimeSpan(11, 30, 00),
                    endTime     = new TimeSpan(12, 0, 0),
                    language    = (int)Languages.en
                },

                new OpeningHour
                {
                    id = 3,
                    day = 2,
                    startTime = new TimeSpan(9, 30, 0),
                    endTime     = new TimeSpan(18, 0, 0),
                    language = GetAllLanguages().SingleOrDefault(l => l.name == "עברית").id
                },

                new OpeningHour
                {
                    id = 4,
                    day = 12,
                    startTime = new TimeSpan(9, 30, 0),
                    endTime     = new TimeSpan(18, 0, 0),
                    language = (int)Languages.en
                },

                new OpeningHour
                {
                    id = 5,
                    day = 3,
                    startTime = new TimeSpan(9, 30, 0),
                    endTime     = new TimeSpan(18, 0, 0),
                    language = GetAllLanguages().SingleOrDefault(l => l.name == "עברית").id
                },

                new OpeningHour
                {
                    id = 6,
                    day = 13,
                    startTime = new TimeSpan(9, 30, 0),
                    endTime     = new TimeSpan(18, 0, 0),
                    language = (int)Languages.en
                },

                new OpeningHour
                {
                    id = 7,
                    day = 4,
                    startTime = new TimeSpan(9, 30, 0),
                    endTime     = new TimeSpan(18, 0, 0),
                    language = GetAllLanguages().SingleOrDefault(l => l.name == "עברית").id
                },

                new OpeningHour
                {
                    id = 8,
                    day = 14,
                    startTime = new TimeSpan(9, 30, 0),
                    endTime     = new TimeSpan(18, 0, 0),
                    language = (int)Languages.en
                },

                new OpeningHour
                {
                    id = 9,
                    day = 5,
                    startTime = new TimeSpan(9, 30, 0),
                    endTime     = new TimeSpan(18, 0, 0),
                    language = GetAllLanguages().SingleOrDefault(l => l.name == "עברית").id
                },

                new OpeningHour
                {
                    id = 10,
                    day = 15,
                    startTime = new TimeSpan(9, 30, 0),
                    endTime     = new TimeSpan(18, 0, 0),
                    language = (int)Languages.en
                },

                new OpeningHour
                {
                    id = 11,
                    day = 7,
                    startTime = new TimeSpan(10, 45, 0),
                    endTime     = new TimeSpan(18, 0, 0),
                    language = GetAllLanguages().SingleOrDefault(l => l.name == "עברית").id
                },

                new OpeningHour
                {
                    id = 12,
                    day = 17,
                    startTime = new TimeSpan(9, 45, 0),
                    endTime     = new TimeSpan(18, 0, 0),
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

        public override DbSet<EnclosureDetail> GetAllEnclosureDetails()
        {
            return AllEnclosursDetails;
        }

        public override DbSet<AnimalDetail> GetAllAnimalsDetails()
        {
            return AllAnimalsDetails;
        }

        public override DbSet<EnclosurePicture> GetAllEnclosurePictures()
        {
            return AllEnclosurePictures;
        }

        public override DbSet<YoutubeVideoUrl> GetAllEnclosureVideos()
        {
            return AllYoutubeVideoUrls;
        }

        public override DbSet<Device> getAllDevices()
        {
            return AllDevices;
        }
        #endregion
    }
}
