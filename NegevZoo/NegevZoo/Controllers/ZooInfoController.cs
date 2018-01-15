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
        /// <param name="language">The data language</param>
        /// <returns>All WallFeed with that language.</returns>
        [HttpGet]
        [Route("wallfeed/{language}")]
        public IEnumerable<WallFeed> getWallFeed(int language = 1)
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

        #endregion
    }

}
