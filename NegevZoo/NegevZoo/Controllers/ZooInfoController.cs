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

        #region Price
        /// <summary>
        /// Gets all the Price with data in that language.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <returns>All Prices with that language.</returns>
        [HttpGet]
        [Route("prices/{language}")]
        public IEnumerable<Price> GetAllPrices(int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAllPrices(language);
                }

            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Adds or Updates the Price element.
        /// </summary>
        /// <param name="price">The element to add or update</param>
        /// <param name="language">The data language. Default is Hebrew</param>
        [HttpPost]
        [Route("prices/update")]
        public void UpdatePrice(Price price)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdatePrice(price);
                }

            }
            catch
            {
            }
        }

        /// <summary>
        /// Deletes the Price element.
        /// </summary>
        /// <param name="id">The element's id to delete</param>
        [HttpDelete]
        [Route("prices/{id}")]
        public void DeletePrice(int id)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.DeletePrice(id);
                }

            }
            catch
            {
            }
        }
        #endregion

        #region Opening hours
        /// <summary>
        /// Gets all the OpeningGours elements with data in that language.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <returns>All OpeninngHour elements with that language.</returns>
        [HttpGet]
        [Route("OpeningHours/{language}")]
        public IEnumerable<OpeningHour> GetAllOpeningHours(int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAllOpeningHours(language);
                }

            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Adds or Updates the OpeningHour element.
        /// </summary>
        /// <param name="openingHour">The element to add or update</param>
        [HttpGet]
        [Route("OpeningHours/update")]
        public void UpdateOpeningHour(OpeningHour openingHour)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdateOpeningHour(openingHour);
                }

            }
            catch
            {
            }
        }

        /// <summary>
        /// Deletes the OpeningHour element.
        /// </summary>
        /// <param name="id">The element's id to delete</param>
        [HttpDelete]
        [Route("OpeningHours/{id}")]
        public void DeleteOpeningHour(int id)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.DeleteOpeningHour(id);
                }

            }
            catch
            {
            }
        }

        #endregion

        #region Contact Info
        /// <summary>
        /// Gets all the ContactInfo elements with data in that language.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <returns>All ContactInfo elements with that language.</returns>
        [HttpGet]
        [Route("ContactInfos/{language}")]
        public IEnumerable<ContactInfo> GetAllContactInfos(int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAllContactInfos(language);
                }

            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Adds or Updates the ContactInfo element.
        /// </summary>
        /// <param name="contactInfo">The element to add or update</param>
        [HttpGet]
        [Route("ContactInfos/update")]
        public void UpdateContactInfo(ContactInfo contactInfo)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdateContactInfo(contactInfo);
                }

            }
            catch
            {
            }
        }

        /// <summary>
        /// Deletes the ContactInfo element.
        /// </summary>
        /// <param name="id">The element's id to delete</param>
        [HttpDelete]
        [Route("ContactInfos/{id}")]
        public void DeleteContactInfo(int id)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.DeleteContactInfo(id);
                }

            }
            catch
            {
            }
        }

        #endregion

        #region Special events
        /// <summary>
        /// Gets all the SpecialEvent elements with data in that language.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <returns>All SpecialEvent elements with that language.</returns>
        [HttpGet]
        [Route("SpecialEvents/{language}")]
        public IEnumerable<SpecialEvent> GetAllSpecialEvents(int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAllSpecialEvents(language);
                }

            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Gets SpecialEvent elements between the wanted dates with data in that language.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <param name="startDate">The start date to look for</param>
        /// <param name="endDate">The end date to look for</param>
        /// <returns>All SpecialEvent elements with that language.</returns>
        [HttpGet]
        [Route("SpecialEvents/{startDate}/{endDate}/{language}")]
        public IEnumerable<SpecialEvent> GetAllSpecialEventsByDates(DateTime startDate, DateTime endDate, int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetSpecialEventsByDate(startDate, endDate ,language);
                }

            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Adds or Updates the SpecialEvent element.
        /// </summary>
        /// <param name="specialEvent">The element to add or update</param>
        [HttpPost]
        [Route("SpecialEvents")]
        public void UpdateSpecialEvent(SpecialEvent specialEvent)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdateSpecialEvent(specialEvent);
                }

            }
            catch
            {
            }
        }

        /// <summary>
        /// Deletes the SpecialEvent element.
        /// </summary>
        /// <param name="id">The element's id to delete</param>
        [HttpDelete]
        [Route("SpecialEvents/{id}")]
        public void DeleteSpecialEvent(int id)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.DeleteSpecialEvent(id);
                }

            }
            catch
            {
            }
        }

        #endregion

        #region Wall Feed

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
                    return db.GetAllWallFeeds(language);
                }

            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// Add or updates the WallFeed.
        /// </summary>
        /// <param name="feed">The WallFeed to add or update</param>
        [HttpPost]
        [Route("wallfeed/update")]
        public void UpdateWallFeed(WallFeed feed)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdateWallFeed(feed);
                }

            }
            catch
            {
            }
        }

        /// <summary>
        /// Removes a WallFeed.
        /// </summary>
        /// <param name="id">The WallFeed's id to deletes</param>
        [HttpDelete]
        [Route("wallfeed/{id}")]
        public void DeleteWallFeed(int id)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.DeleteWallFeed(id);
                }

            }
            catch
            {
            }
        }
        #endregion

        #region General Info
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

        /// <summary>
        /// Adds or Updates the zoo's about info.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <param name="info">The info to add or update</param>
        [HttpPost]
        [Route("about/{info}")]
        [Route("about/{info}/{language}")]
        public void UpdateZooAboutInfo(string info, int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdateZooAboutInfo(info, language);
                }
            }
            catch
            {
            }
        }

        /// <summary>
        /// Gets the zoo's openingHourNote.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <returns>The zoo's opening hour note.</returns>
        [HttpGet]
        [Route("about/{language}")]
        public IEnumerable<AboutUsResult> GetOpeningHourNote(int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetOpeningHourNote(language)
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

        /// <summary>
        /// Adds or Updates the zoo's openingHourNote.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <param name="note">The note to add or update</param>
        [HttpPost]
        [Route("about/{note}")]
        [Route("about/{note}/{language}")]
        public void UpdateOpeningHourNote(string note, int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdateOpeningHourNote(note, language);
                }
            }
            catch
            {
            }
        }

        /// <summary>
        /// Gets the zoo's ContactInfoNote.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <returns>The zoo's Contact us note.</returns>
        [HttpGet]
        [Route("about/{language}")]
        public IEnumerable<AboutUsResult> GetContactInfoNote(int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetContactInfoNote(language)
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

        /// <summary>
        /// Adds or Updates the zoo's ContactInfoNote.
        /// </summary>
        /// <param name="language">The data language. Default is Hebrew</param>
        /// <param name="note">The note to add or update</param>
        [HttpPost]
        [Route("about/{note}")]
        [Route("about/{note}/{language}")]
        public void UpdateContactInfoNote(string note, int language = 1)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdateContactInfoNote(note, language);
                }
            }
            catch
            {
            }
        }
        #endregion
        
        #region ModelClasses
        //This inner class is so we will be able to return a primitive object via http get
        public class AboutUsResult
        {
            public String aboutUs { get; set; }
        }

        #endregion
    }

    

    
}
