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
            Languages           = new TestDbSet<Language>();
            EnclosursDetails    = new TestDbSet<EnclosureDetail>();
            AnimalsDetails      = new TestDbSet<AnimalDetail>();
            EnclosurePictures   = new TestDbSet<EnclosurePicture>();
            YoutubeVideoUrls    = new TestDbSet<YoutubeVideoUrl>();
            Devices             = new TestDbSet<Device>();
            MapInfo             = new TestDbSet<MapInfo>();
            Routes              = new TestDbSet<Route>();
            MiscMarkers         = new TestDbSet<MiscMarker>();

            Languages.AddRange(InitializeLanguages());
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
            EnclosursDetails.AddRange(InitializeEnclosureDetails());
            EnclosurePictures.AddRange(InitializeEnclosurePictures());
            YoutubeVideoUrls.AddRange(InitializeYouTubeVidoes());
            AnimalsDetails.AddRange(InitializeAnimalsDetails());
            Devices.AddRange(InitializeDevices());
            MapInfo.AddRange(InitalizeMapInfo());
            Routes.AddRange(InitalizeRoutes());
            MiscMarkers.AddRange(InitializeMiscMarkers());
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
        protected DbSet<MiscMarker> MiscMarkers { get; set; }
        protected DbSet<ContactInfo> ContactInfos { get; set; }
        protected DbSet<SpecialEvent> SpecialEvents { get; set; }
        protected DbSet<User> Users { get; set; }
        protected DbSet<Language> Languages { get; set; }
        protected DbSet<EnclosureDetail> EnclosursDetails { get; set; }
        protected DbSet<AnimalDetail> AnimalsDetails { get; set; }
        protected DbSet<EnclosurePicture> EnclosurePictures { get; set; }
        protected DbSet<YoutubeVideoUrl> YoutubeVideoUrls { get; set; }
        protected DbSet<Device> Devices { get; set; }
        protected DbSet<MapInfo> MapInfo { get; set; }
        protected DbSet<Route> Routes { get; set; }

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

        /// <summary>
        /// Initializes the enclosure details mock.
        /// </summary>
        /// <returns>Mock EnclosureDetails list.</returns>
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

        /// <summary>
        /// Initializes the enclosures pictures mock.
        /// </summary>
        /// <returns>Mock EnclosurePictures list.</returns>
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

        /// <summary>
        /// Initializes the enclosure videos mock.
        /// </summary>
        /// <returns>Mock EnclosureVideos list.</returns>
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
        /// Initializes the enclosure's RecurringEvents mock.
        /// </summary>
        /// <returns>Mock RecurringEvetns list.</returns>
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
                    day                     = 11,
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
                    language        = Languages.SingleOrDefault(l => l.id == 1).id, //hebrew
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
                    language        = Languages.SingleOrDefault(l => l.id == 2).id, //english
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
                    language        = Languages.SingleOrDefault(l => l.id == 1).id, //hebrew
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
                    language        = Languages.SingleOrDefault(l => l.id == 2).id, //english
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
                    language        = Languages.SingleOrDefault(l => l.id == 1).id, //hebrew
                    name            = "קוף",
                    category        = "קופים",
                    series          = "קוף",
                    distribution    = "",
                    family          = "",
                    food            = "",
                    reproduction    = ""
                },
                new AnimalDetail
                {
                    animalId        = 3,
                    language        = Languages.SingleOrDefault(l => l.id == 2).id, //english
                    name            = "Monkey",
                    category        = "Monkies",
                    series          = "Monk",
                    distribution    = "",
                    family          = "",
                    food            = "",
                    reproduction    = ""
                },
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
                        language    = (int)DAL.Languages.en
                    },

                    new Price
                    {
                        id          = 2,
                        population  = "מבוגר",
                        pricePop    = 40,
                        language    = (int)DAL.Languages.he
                    },

                    new Price
                    {
                        id          = 3,
                        population  = "Children under 18",
                        pricePop    = 25,
                        language    = (int)DAL.Languages.en
                    },

                    new Price
                    {
                        id          = 4,
                        population  = "ילד מתחת לגיל 18",
                        pricePop    = 25,
                        language    = (int)DAL.Languages.he
                    },

                    new Price
                    {
                        id          = 5,
                        population  = "Soldier",
                        pricePop    = 25,
                        language    = (int)DAL.Languages.en
                    },

                    new Price
                    {
                        id          = 6,
                        population  = "חייל",
                        pricePop    = 25,
                        language    = (int)DAL.Languages.he
                    },

                    new Price
                    {
                        id          = 7,
                        population  = "Pensioner",
                        pricePop    = 10,
                        language    = (int)DAL.Languages.en
                    },

                    new Price
                    {
                        id          = 8,
                        population  = "פנסיונר",
                        pricePop    = 25,
                        language    = (int)DAL.Languages.he
                    },

                    new Price
                    {
                        id          = 9,
                        population  = "Student",
                        pricePop    = 10,
                        language    = (int)DAL.Languages.en
                    },

                    new Price
                    {
                        id          = 10,
                        population  = "סטודנט",
                        pricePop    = 25,
                        language    = (int)DAL.Languages.he
                    }
            };
        }

        /// <summary>
        /// Initializes the opening hour mock.
        /// </summary>
        /// <returns>Mock OpeningHour list.</returns>
        private IEnumerable<OpeningHour> InitialOpeningHour()
        {
            return new List<OpeningHour>
            {
                //sunday
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
                    language    = (int)DAL.Languages.en
                },

                //monday
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
                    language = (int)DAL.Languages.en
                },

                //
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
                    language = (int)DAL.Languages.en
                },

                //
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
                    language = (int)DAL.Languages.en
                },

                //
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
                    language = (int)DAL.Languages.en
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
                    language = (int)DAL.Languages.en
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
                        title       = "Purim Events",
                        info        = "Our Purim Events started!",
                        language    = (int)DAL.Languages.en,
                        
                    },

                    new WallFeed
                    {
                        id          = 2,
                        title       = "אירועי פורים",
                        info        = "אירועי פורים שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 3,
                        title       = "Passeover Events",
                        info        = "Our Passeover Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {

                        id          = 4,
                        title       = "אירועי פסח",
                        info        = "אירועי פסח שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 5,
                        title       = "Shavuut Events",
                        info        = "Our Shavuut Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {
                        id          = 6,
                        title       = "אירועי שבועות",
                        info        = "אירועי שבועות שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 7,
                        title       = "Sukut Events",
                        info        = "Our Sukut Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {
                        id          = 8,
                        title       = "אירועי סוכות",
                        info        = "אירועי סוכות שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },
                    new WallFeed
                    {
                        id          = 9,
                        title       = "Purim Events",
                        info        = "Our Purim Events started!",
                        language    = (int)DAL.Languages.en,

                    },

                    new WallFeed
                    {
                        id          = 10,
                        title       = "אירועי פורים",
                        info        = "אירועי פורים שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 11,
                        title       = "Passeover Events",
                        info        = "Our Passeover Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {

                        id          = 12,
                        title       = "אירועי פסח",
                        info        = "אירועי פסח שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 13,
                        title       = "Shavuut Events",
                        info        = "Our Shavuut Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {
                        id          = 14,
                        title       = "אירועי שבועות",
                        info        = "אירועי שבועות שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 15,
                        title       = "Sukut Events",
                        info        = "Our Sukut Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {
                        id          = 16,
                        title       = "אירועי סוכות",
                        info        = "אירועי סוכות שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },
                    new WallFeed
                    {
                        id          = 17,
                        title       = "Purim Events",
                        info        = "Our Purim Events started!",
                        language    = (int)DAL.Languages.en,

                    },

                    new WallFeed
                    {
                        id          = 18,
                        title       = "אירועי פורים",
                        info        = "אירועי פורים שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 19,
                        title       = "Passeover Events",
                        info        = "Our Passeover Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {

                        id          = 20,
                        title       = "אירועי פסח",
                        info        = "אירועי פסח שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 21,
                        title       = "Shavuut Events",
                        info        = "Our Shavuut Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {
                        id          = 22,
                        title       = "אירועי שבועות",
                        info        = "אירועי שבועות שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 23,
                        title       = "Sukut Events",
                        info        = "Our Sukut Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {
                        id          = 24,
                        title       = "אירועי סוכות",
                        info        = "אירועי סוכות שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },
                    new WallFeed
                    {
                        id          = 25,
                        title       = "Purim Events",
                        info        = "Our Purim Events started!",
                        language    = (int)DAL.Languages.en,

                    },

                    new WallFeed
                    {
                        id          = 26,
                        title       = "אירועי פורים",
                        info        = "אירועי פורים שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 27,
                        title       = "Passeover Events",
                        info        = "Our Passeover Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {

                        id          = 28,
                        title       = "אירועי פסח",
                        info        = "אירועי פסח שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 29,
                        title       = "Shavuut Events",
                        info        = "Our Shavuut Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {
                        id          = 30,
                        title       = "אירועי שבועות",
                        info        = "אירועי שבועות שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },

                    new WallFeed
                    {
                        id          = 31,
                        title       = "Sukut Events",
                        info        = "Our Sukut Events started!",
                        language    = (int)DAL.Languages.en,
                    },

                    new WallFeed
                    {
                        id          = 32,
                        title       = "אירועי סוכות",
                        info        = "אירועי סוכות שלנו החלו!",
                        language    = (int)DAL.Languages.he,
                    },
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
                        mapBackgroundUrl    = "MapUrl",
                        language            = (int)DAL.Languages.en
                    },

                    new GeneralInfo
                    {
                        name                = "נגב זו",
                        aboutUs             = "אנחנו נגב זו!!! אנחנו אוהבים חיות",
                        contactInfoNote     = "ניתן ליצור קשר בין השעות 08:00 לבין 22:00",
                        openingHoursNote    = "הקופות יסגרו שעתיים לפני סגירת הגן",
                        mapBackgroundUrl    = "MapUrl",
                        language            = (int)DAL.Languages.he
                    }
            };
        }

        /// <summary>
        /// Initializes the SpecialEvents mock.
        /// </summary>
        /// <returns>Mock Special Events list.</returns>
        private IEnumerable<SpecialEvent> InitialSpecialEvents()
        {
            return new List<SpecialEvent>
            {
                new SpecialEvent
                {
                    id                      = 1,
                    title                   = "כותרת1",
                    description             = "1קקי",
                    startDate               = new DateTime(2018,3,5),
                    endDate                 = new DateTime(2018,3,8),
                    language                = (int) DAL.Languages.he
                },
                new SpecialEvent
                {
                    id                      = 2,
                    title                   = "title1",
                    description             = "Kaki1",
                    startDate               = new DateTime(2018,3,5),
                    endDate                 = new DateTime(2018,3,8),
                    language                = (int) DAL.Languages.en
                },
                new SpecialEvent
                {
                    id                      = 3,
                    title                   = "אירועי פורים",
                    description             = "ישנם אירועים רבים ומגוונים",
                    startDate               = new DateTime(2018,3,1),
                    endDate                 = new DateTime(2018,3,8),
                    language                = (int) DAL.Languages.he
                },
                new SpecialEvent
                {
                    id                      = 4,
                    title                   = "Purim Events",
                    description             = "There are many kinds of events",
                    startDate               = new DateTime(2018,3,1),
                    endDate                 = new DateTime(2018,3,8),
                    language                = (int) DAL.Languages.en
                }
            };
        }
        
        /// <summary>
        /// Initializes the ContactInfo mock.
        /// </summary>
        /// <returns>Mock ContactInfo list.</returns>
        private IEnumerable<ContactInfo> InitialContactInfos()
        {
            return new List<ContactInfo>
            {
                new ContactInfo
                {
                    id          = 1,
                    via         = "טלפון",
                    address     = "08-641-4777",
                    language    =(int)DAL.Languages.he
                },

                new ContactInfo
                {
                    id          = 2,
                    via         = "Phone",
                    address     = "08-641-4777",
                    language    =(int)DAL.Languages.en
                },

                new ContactInfo
                {
                    id          = 3,
                    via         = "דואר",
                    address     = "דרך אילן רמון 5, באר שבע",
                    language    =(int)DAL.Languages.he
                },

                new ContactInfo
                {
                    id          = 4,
                    via         = "Post Mail",
                    address     = "Via Ilan Ramon 5, Beer-Sheva",
                    language    =(int)DAL.Languages.en
                },

                new ContactInfo
                {
                    id          = 5,
                    via         = "דואר אלקטרוני",
                    address     = "gilorisr@post.bgu.ac.il",
                    language    =(int)DAL.Languages.he
                },

                new ContactInfo
                {
                    id          = 6,
                    via         = "E-Mail",
                    address     = "gilorisr@post.bgu.ac.il",
                    language    =(int)DAL.Languages.en
                }
            };
        }

        /// <summary>
        /// Initializes the Languages mock.
        /// </summary>
        /// <returns>Mock enclosures list.</returns>
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

        /// <summary>
        /// Initializes the users mock.
        /// </summary>
        /// <returns>Mock Users list.</returns>
        private IEnumerable<User> InitializeUsers()
        {
            return new List<User>
            {
                new User
                {
                    id          = 1,
                    isAdmin     = true,
                    name        = "אור",
                    password    = "6b136e22312515c4e45986a40188ce91", //password is 123
                    salt        = "kaki"
                },
                new User
                {
                    id          = 2,
                    isAdmin     = false,
                    name        = "גיל",
                    password    = "123",
                    salt        = "kaki"
                },
                new User
                {
                    id          = 3,
                    isAdmin     = true,
                    name        = "מנהל",
                    password    = "123",
                    salt        = "kaki"
                },
                new User
                {
                    id          = 4,
                    isAdmin     = false,
                    name        = "עובד",
                    password    = "123",
                    salt        = "kaki"
                }
            };
        }

        /// <summary>
        /// Initializes the devices mock.
        /// </summary>
        /// <returns>Mock devices list.</returns>
        private IEnumerable<Device> InitializeDevices()
        {
            return new List<Device>
            {
                new Device
                {
                    id = 1,
                    deviceId = "123",
                    insidePark = (sbyte)1,
                    lastPing = new DateTime(2018,03,25,10,00,00)
                },
                new Device
                {
                    id = 2,
                    deviceId = "1234",
                    insidePark = (sbyte)1,
                    lastPing = DateTime.Now
                }
            };
        }

        private IEnumerable<MapInfo> InitalizeMapInfo()
        {
            return new List<MapInfo>
            {

            };
        }

        private IEnumerable<Route> InitalizeRoutes()
        {
            return new List<Route>
            {

            };
        }

        private IEnumerable<MiscMarker> InitializeMiscMarkers()
        {
            return new List<MiscMarker>
            {
                new MiscMarker
                {
                    id          = 0,
                    longitude   = 100,
                    latitude    = 200,
                    iconUrl     = "someUrl"
                },
                new MiscMarker
                {
                    id          = 1,
                    longitude   = 300,
                    latitude    = 500,
                    iconUrl     = "someUrl2"
                }
            };
        }

        #endregion

        #region Getters

        public override DbSet<Animal> GetAllAnimals()
        {
            return Animals;
        }

        public override DbSet<MiscMarker> GetAllMiscMarkers()
        {
            return MiscMarkers;
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
            return Languages;
        }

        public override DbSet<EnclosureDetail> GetAllEnclosureDetails()
        {
            return EnclosursDetails;
        }

        public override DbSet<AnimalDetail> GetAllAnimalsDetails()
        {
            return AnimalsDetails;
        }

        public override DbSet<EnclosurePicture> GetAllEnclosurePictures()
        {
            return EnclosurePictures;
        }

        public override DbSet<YoutubeVideoUrl> GetAllEnclosureVideos()
        {
            return YoutubeVideoUrls;
        }

        public override DbSet<Device> GetAllDevices()
        {
            return Devices;
        }

        public override DbSet<MapInfo> GetAllMapInfos()
        {
            return MapInfo;
        }

        public override DbSet<Route> GetAllRoutes()
        {
            return Routes;
        }
        #endregion
    }
}
