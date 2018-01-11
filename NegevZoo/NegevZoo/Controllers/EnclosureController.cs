using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Backend.Models;
using DAL;

namespace NegevZoo.Controllers
{
    /// <summary>
    /// Controller that holds all enclosure functionality.
    /// </summary>
    public class EnclosureController : ControllerBase
    {
        #region Getters

        /// <summary>
        /// Gets all the enclosures with data in that language.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <returns>All enclosures with that language.</returns>
        [HttpGet]
        [Route("enclosures/{language}")]
        public IEnumerable<Enclosure> GetAllEnclosures(int language = 1)
        {
            try
            {
                using (var db = new ZooContext())
                {
                    return db.GetEnclosures(language);
                }

            }
            catch
            {
                return null;
            }
        }

        #endregion

        #region Setters

        /// <summary>
        /// Sets the enclosures in that park.
        /// </summary>
        /// <param name="enclosures">The enclosures to set.</param>
        [HttpPut]
        [Route("enclosures")]
        public void UpdateEnclosures(IEnumerable<Enclosure> enclosures)
        {
            try
            {
                using (var db = new ZooContext())
                {
                    db.AddEnclosures(enclosures);
                }

            }
            catch
            {
            }
        }
        
        #endregion
    }
}
