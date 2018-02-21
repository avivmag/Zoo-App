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
                    return db.GetAllEnclosures(language);
                }

            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Gets enclosure by it's id.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="id">The eclosure's id</param>
        /// <returns>The enclosures with this id and language.</returns>
        [HttpGet]
        [Route("enclosures/{id}/{language}")]
        public Enclosure GetEnclosureById(int id, int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetEnclosureById(id, language);
                }

            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Gets enclosure by it's name.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="name">The eclosure's name</param>
        /// <returns>The enclosures with this name and language.</returns>
        [HttpGet]
        [Route("enclosures/{name}/{language}")]
        public Enclosure GetEnclosureByname(string name, int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetEnclosureByName(name, language);
                }

            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Gets enclosure by it's position.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="langtitude">The eclosure's langtitude</param>
        /// <param name="latitude">The eclosure's latitude</param>
        /// <returns>The enclosures with this aproximate position.</returns>
        [HttpGet]
        [Route("enclosures/{longtitude}/{latitude}/{language}")]
        public Enclosure GetEnclosureByPosition(int longtitude, int latitude, int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetEnclosureByPosition(longtitude, latitude, language);
                }

            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Gets the enclosure's recurring events by it's id.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="id">The eclosure's id</param>
        /// <returns>The recurring events according to the enclosure id.</returns>
        [HttpGet]
        [Route("enclosures/{id}/{language}")]
        public IEnumerable<RecurringEvent> GetRecurringEvents(int id, int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetRecurringEvents(id, language);
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
        [HttpPost]
        [Route("enclosures/update")]
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

        /// <summary>
        /// Deletes an enclosure.
        /// </summary>
        /// <param name="id">The enclosure's id to delete.</param>
        [HttpPost]
        [Route("enclosures/{id}")]
        public void DeleteEnclosure(int id)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.DeleteEnclosure(id);
                }

            }
            catch
            {
            }
        }

        #endregion
    }
}
