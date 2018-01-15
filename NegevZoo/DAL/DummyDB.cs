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
            this.Animals            = new TestDbSet<Animal>();
            this.Enclosures         = new TestDbSet<Enclosure>();
            this.WallFeeds          = new TestDbSet<WallFeed>();
            this.Prices             = new TestDbSet<Price>();
            this.GeneralInfo        = new TestDbSet<GeneralInfo>();

            this.Animals.AddRange(InitializeAnimals());
            this.Enclosures.AddRange(InitializeEnclosures());
            this.WallFeeds.AddRange(InitializeWallFeeds());
            this.Prices.AddRange(InitializePrices());
            this.Prices.AddRange(InitializePrices());
            this.Prices.AddRange(InitializePrices());
            this.Prices.AddRange(InitializePrices());
            this.Prices.AddRange(InitializePrices());
            this.Prices.AddRange(InitializePrices());
            this.Prices.AddRange(InitializePrices());
            this.GeneralInfo.AddRange(InitializeGeneralInfo());
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
                        Id          = 1,
                        Name        = "בבון הזית",
                        EncId       = 1,
                        Story       = "גילאור בבון הזית מאוד חמוד",
                        Language    = (int)Languages.he
                    },

                    new Animal
                    {
                        Id          = 2,
                        Name        = "Gorilla",
                        EncId       = 1,
                        Story       = "Shrek the mighty gorilla!",
                        Language    = (int)Languages.en
                    },

                    new Animal
                    {
                        Id          = 2,
                        Name        = "גורילה",
                        EncId       = 1,
                        Story       = "שרק הוא וואחד גורילה!",
                        Language    = (int)Languages.he
                    },

                    new Animal
                    {
                        Id          = 3,
                        Name        = "Monkey",
                        EncId       = 1,
                        Story       = "Kofifo is Marco's monkey.",
                        Language    = (int)Languages.en
                    },

                    new Animal
                    {
                        Id          = 3,
                        Name        = "קוף",
                        EncId       = 1,
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
                        Id          = 1,
                        Name        = "תצוגת הקופים",
                        Story       = "הקופים שלנו הם הכי הכי!",
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
        #endregion
    }
}
