using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Backend.Models;
using BL;

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
                using (var db = this.GetContext())
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
        /// Adds or updates an enclosure.
        /// </summary>
        /// <param name="enclosures">The enclosures to update.</param>
        [HttpPut]
        [Route("enclosures")]
        public void UpdateEnclosure(Enclosure enclosure)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.UpdateEnclosure(enclosure);
                }

            }
            catch
            {
            }
        }
        
        #endregion
    }
}
