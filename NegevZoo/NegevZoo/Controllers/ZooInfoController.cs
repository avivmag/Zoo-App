using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Backend.Models;

namespace NegevZoo.Controllers
{
    public class ZooInfoController : ControllerBase
    {
        // TODO:: Under construction.
        #region Getters

        /// <summary>
        /// Gets all the WallFeed with data in that language.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <returns>All WallFeed with that language.</returns>
        [HttpGet]
        [Route("wallfeed/{language}")]
        public IEnumerable<WallFeed> GetAllFeeds(int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetWallFeed(language);
                }

            }
            catch
            {
                return null;
            }
        }


        /// <summary>
        /// Gets all the Price with data in that language.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <returns>All Prices with that language.</returns>
        [HttpGet]
        [Route("prices/{language}")]
        public IEnumerable<Price> GetPrices(int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetPrices(language);
                }

            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Gets the zoo's about info.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <returns>The zoo's about info.</returns>
        [HttpGet]
        [Route("about/{language}")]
        public IEnumerable<AboutUsResult> GetZooAboutInfo(int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetZooAboutInfo(language)
                        .Select(zi =>
                            new AboutUsResult
                            {
                                aboutUs = zi
                            })
                        .ToArray();
                }
            }
            catch
            {
                return null;
            }
        }


        #endregion

        #region Setters

        #endregion

        #region ModelClasses

        public class AboutUsResult
        {
            public String aboutUs { get; set; }
        }

        #endregion
    }

    

    
}
