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
        /// Gets enclosure by it's encId.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="id">The eclosure's encId</param>
        /// <returns>The enclosures with this encId and language.</returns>
        [HttpGet]
        [Route("enclosures/{encId}/{language}")]
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
        public IEnumerable<Enclosure> GetEnclosureByName(string name, int language = 1)
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
        /// Gets the enclosure's recurring events by it's encId.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="encId">The eclosure's encId</param>
        /// <returns>The recurring events according to the enclosure encId.</returns>
        [HttpGet]
        [Route("enclosures/{encId}/recurring/{language}")]
        public IEnumerable<RecurringEvent> GetRecurringEvents(int encId, int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetRecurringEvents(encId, language);
                }

            }
            catch 
            {
                //TODO: add to log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
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
                //TODO add a log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));

            }
        }

        /// <summary>
        /// Deletes an enclosure.
        /// </summary>
        /// <param name="id">The enclosure's encId to delete.</param>
        [HttpPost]
        [Route("enclosures/{encId}")]
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
                //TODO: add log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
        #endregion
    }
}
