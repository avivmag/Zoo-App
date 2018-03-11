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
        [Route("enclosures/all/{language}")]
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
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets enclosure by it's encId.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="id">The eclosure's encId</param>
        /// <returns>The enclosures with this encId and language.</returns>
        [HttpGet]
        [Route("enclosures/id/{encId}/{language}")]
        public Enclosure GetEnclosureById(int encId, int language = 1)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    return db.GetEnclosureById(encId, language);
                }

            }
            catch
            {
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets enclosure by it's name.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="name">The eclosure's name</param>
        /// <returns>The enclosures with this name and language.</returns>
        [HttpGet]
        [Route("enclosures/name/{name}/{language}")]
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
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
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
        [Route("enclosures/position/{longtitude}/{latitude}/{language}")]
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
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Gets the enclosure's recurring events by it's encId.
        /// </summary>
        /// <param name="language">The data language</param>
        /// <param name="encId">The eclosure's encId</param>
        /// <returns>The recurring events according to the enclosure encId.</returns>
        [HttpGet]
        [Route("enclosures/recurring/{encId}/{language}")]
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
        [HttpDelete]
        [Route("enclosures/delete/{encId}")]
        public void DeleteEnclosure(int encId)
        {
            try
            {
                using (var db = this.GetContext())
                {
                    db.DeleteEnclosure(encId);
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
