﻿using System;
using System.Collections.Generic;
using System.Linq;
using Backend.Models;
using DAL;

namespace BL
{
    public class ZooContext : IDisposable 
    {
        private IZooDB zooDB;

        // TODO:: Mark isTesting to false when the database it ready.
        public ZooContext(bool isTesting = true)
        {
            if (isTesting)
            {
                zooDB = new DummyDB();
            }
            else
            {
                // TODO:: whenever the database will be ready - initialize it here.
            }
        }

        #region Enclosure

        /// <summary>
        /// Gets the enclosures.
        /// </summary>
        /// <param name="language">The enclosure's data language.</param>
        /// <returns>The enclosures.</returns>
        public IEnumerable<Enclosure> GetAllEnclosures(int language)
        {
            return zooDB.GetAllEnclosures().Where(e => e.Language == language).ToArray();
        }

        /// <summary>
        /// Gets the enclosure by id.
        /// </summary>
        /// <param name="language">The enclosure's data language.</param>
        /// <param name="id">The enclosure's id.</param>
        /// <returns>The enclosure.</returns>
        public Enclosure GetEnclosureById(int id, int language)
        {
            return zooDB.GetAllEnclosures().SingleOrDefault(e => e.Language == language && e.Id == id);
        }

        /// <summary>
        /// Gets the enclosure by it's name.
        /// </summary>
        /// <param name="language">The enclosure's data language.</param>
        /// <param name="name">The enclosure's name.</param>
        /// <returns>The enclosure.</returns>
        public Enclosure GetEnclosureByName(string name, int language)
        {
            return zooDB.GetAllEnclosures().SingleOrDefault(e => e.Language == language && e.Name == name);
        }

        /// <summary>
        /// Gets the enclosure by longtitude and latitude.
        /// </summary>
        /// <param name="language">The enclosure's data language.</param>
        /// <param name="longtitud">The enclosure's longtitude.</param>
        /// <param name="latitude">The enclosure's latitude.</param>
        /// <returns>The enclosure.</returns>
        public Enclosure GetEnclosureByPosition(double longtitud, double latitude, int language)
        {
            return zooDB.GetAllEnclosures().SingleOrDefault(e => e.Language == language &&
                                                            (e.Longtitude <= longtitud + 5 && e.Longtitude >= longtitud - 5) &&
                                                            (e.Latitude <= latitude + 5 && e.Latitude >= latitude - 5) );
        }

        /// <summary>
        /// Gets the enclosure's recurring events by it's id.
        /// </summary>
        /// <param name="language">The enclosure's data language.</param>
        /// <param name="encId">The enclosure's id.</param>
        /// <returns>The enclosure's recurring events.</returns>
        public IEnumerable<RecurringEvent> GetRecurringEvents (int encId, int language)
        {
            return zooDB.GetAllRecuringEvents().Where(re => re.Language == language && re.EncId == encId).ToList();
        }

        /// <summary>
        /// Updates The enclosure.
        /// </summary>
        /// <param name="enclosures">The enclosures to update.</param>
        public void UpdateEnclosure(Enclosure enclosure)
        {
            var enclosures = zooDB.GetAllEnclosures();
            if (!enclosures.Contains(enclosure))
            {
                enclosures.Add(enclosure);
            }
        }

        /// <summary>
        /// Delete The enclosure.
        /// </summary>
        /// <param name="id">The enclosure's id to delete.</param>
        public void DeleteEnclosure(int id)
        {
            Enclosure enclosure = zooDB.GetAllEnclosures().SingleOrDefault(e => e.Id == id);
            if (enclosure != null)
            {
                zooDB.GetAllEnclosures().Remove(enclosure);
            }
        }

        #endregion

        #region Animals

        /// <summary>
        /// Gets all the animals.
        /// </summary>
        /// <param name="language">The animal's data language.</param>
        /// <returns>The animals.</returns>
        public IEnumerable<Animal> GetAnimals(int language)
        {
            return zooDB.GetAnimals().Where(a => a.Language == language).ToArray();
        }

        /// <summary>
        /// Gets animal by Id and language.
        /// </summary>
        /// <param name="language">The data's language</param>
        /// <param name="id">The animal's Id.</param>
        /// <returns>The animal.</returns>
        public Animal GetAnimalById(int id, int language)
        {
            return zooDB.GetAnimals()
                .SingleOrDefault(a => a.Language == language && a.Id == id);
        }

        /// <summary>
        /// Gets animal by Id and language.
        /// </summary>
        /// <param name="language">The data's language</param>
        /// <param name="name">The animal's name.</param>
        /// <returns>The animal.</returns>
        public Animal GetAnimalByName(string name, int language)
        {
            return zooDB.GetAnimals()
                .SingleOrDefault(a => a.Language == language && a.Name == name);
        }

        /// <summary>
        /// Gets animals by enclosure Id and language.
        /// </summary>
        /// <param name="language">The data's language</param>
        /// <param name="encId">The enclosure's Id.</param>
        /// <returns>The animals in the enclosure.</returns>
        public IEnumerable<Animal> GetAnimalsByEnclosure(int encId, int language)
        {
            return zooDB.GetAnimals()
                .Where(a => a.Language == language && a.EncId == encId).ToList();
        }

        /// <summary>
        /// Updates the animal.
        /// </summary>
        /// <param name="animals">The animal to update.</param>
        public void UpdateAnimal(Animal animal)
        {
            var animals = zooDB.GetAnimals();
            if (!animals.Contains(animal))
            {
                animals.Add(animal);
            }
        }

        /// <summary>
        /// Delete the animal.
        /// </summary>
        /// <param name="id">The animal's id to delete.</param>
        public void DeleteAnimal(int id)
        {
            Animal animal = zooDB.GetAnimals().SingleOrDefault(a => a.Id == id);
            if (animal != null)
            {
                zooDB.GetAnimals().Remove(animal);
            }
        }

        #endregion

        #region Zoo Info


        #region Prices
        /// <summary>
        /// Gets all the Price elements.
        /// </summary>
        /// <param name="language">The Price's data language.</param>
        /// <returns>The prices entitiess.</returns>
        public IEnumerable<Price> GetAllPrices(int language)
        {
            return zooDB.GetAllPrices().Where(p => p.Language == language).ToArray();
        }
        
        /// <summary>
        /// Adds or update the Price elements.
        /// </summary>
        /// <param name="price">The Price to add or update.</param>
        public void UpdatePrice(Price price)
        {
            var prices = zooDB.GetAllPrices();
            if (!prices.Contains(price))
            {
                prices.Add(price);
            }
        }

        /// <summary>
        /// Delete the Price elements.
        /// </summary>
        /// <param name="id">The Price's id to delete.</param>
        public void DeletePrice(int id)
        {
            Price price = zooDB.GetAllPrices().SingleOrDefault(p => p.Id == id);
            if (price != null)
            {
                zooDB.GetAllPrices().Remove(price);
            }
        }
        #endregion

        #region OpeningHours
        /// <summary>
        /// Gets all the OpeningHour elements.
        /// </summary>
        /// <param name="language">The OpeningHour's data language.</param>
        /// <returns>All the OpeningHour elemtents.</returns>
        public IEnumerable<OpeningHour> GetAllOpeningHours (int language)
        {
            return zooDB.GetAllOpeningHours().Where(oh => oh.Language == language).ToArray();
         
        }

        /// <summary>
        /// Adds or update the OpeningHour element.
        /// </summary>
        /// <param name="OpeningHour">The OpeningHour element to add or update.</param>
        public void UpdateOpeningHour(OpeningHour openingHour)
        {
            var openingHours = zooDB.GetAllOpeningHours();
            if (!openingHours.Contains(openingHour))
            {
                openingHours.Add(openingHour);
            }
        }

        /// <summary>
        /// Delete the OpeningHour elements.
        /// </summary>
        /// <param name="id">The OpeningHour's id to delete.</param>
        public void DeleteOpeningHour(int id)
        {
            OpeningHour openingHour = zooDB.GetAllOpeningHours().SingleOrDefault(oh => oh.Id == id);
            if (openingHour != null)
            {
                zooDB.GetAllOpeningHours().Remove(openingHour);
            }
        }

        #endregion

        #region ContatInfo
        /// <summary>
        /// Gets all the ContactInfos elements.
        /// </summary>
        /// <param name="language">The ContactInfo's data language.</param>
        /// <returns>All the ContactInfos elemtents.</returns>
        public IEnumerable<ContactInfo> GetAllContactInfos(int language)
        {
            return zooDB.GetAllContactInfos().Where(ci => ci.Language == language).ToArray();

        }

        /// <summary>
        /// Adds or update the ContactInfo element.
        /// </summary>
        /// <param name="contactInfo">The ContactInfo element to add or update.</param>
        public void UpdateContactInfo(ContactInfo contactInfo)
        {
            var contactInfos = zooDB.GetAllContactInfos();
            if (!contactInfos.Contains(contactInfo))
            {
                contactInfos.Add(contactInfo);
            }
        }

        /// <summary>
        /// Delete the ContactInfo elements.
        /// </summary>
        /// <param name="id">The ContactInfo's id to delete.</param>
        public void DeleteContactInfo(int id)
        {
            ContactInfo contactInfo = zooDB.GetAllContactInfos().SingleOrDefault(ci => ci.Id == id);
            if (contactInfo != null)
            {
                zooDB.GetAllContactInfos().Remove(contactInfo);
            }
        }

        #endregion

        #region Special Events
        /// <summary>
        /// Gets all the SpecialEvent elements.
        /// </summary>
        /// <param name="language">The SpecialEvent's data language.</param>
        /// <returns>All the SpecialEvent elemtents.</returns>
        public IEnumerable<SpecialEvent> GetAllSpecialEvents(int language)
        {
            return zooDB.GetAllSpecialEvents().Where(se => se.Language == language).ToArray();
        }

        /// <summary>
        /// Gets SpecialEvent elements between two dates.
        /// </summary>
        /// <param name="language">The SpecialEvent's data language.</param>
        /// <param name="startDate">The start date to look for</param>
        /// <param name="endDate">The end date to look for</param>
        /// <returns>All the SpecialEvent elemtents.</returns>
        public IEnumerable<SpecialEvent> GetSpecialEventsByDate(DateTime startDate, DateTime endDate, int language)
        {
            return zooDB.GetAllSpecialEvents().Where(se => se.Language == language && 
                                                     se.StartDate >= startDate &&
                                                     se.EndDate <= endDate).ToArray();
        }

        /// <summary>
        /// Adds or update the SpecialEvents element.
        /// </summary>
        /// <param name="specialEvent">The SpecialEvent element to add or update.</param>
        public void UpdateSpecialEvent(SpecialEvent specialEvent)
        {
            var specialEvents = zooDB.GetAllSpecialEvents();
            if (!specialEvents.Contains(specialEvent))
            {
                specialEvents.Add(specialEvent);
            }
        }

        /// <summary>
        /// Delete the SpecialEvent elements.
        /// </summary>
        /// <param name="id">The SpecialEvent's id to delete.</param>
        public void DeleteSpecialEvent(int id)
        {
            SpecialEvent specialEvent = zooDB.GetAllSpecialEvents().SingleOrDefault(se => se.Id == id);
            if (specialEvent != null)
            {
                zooDB.GetAllSpecialEvents().Remove(specialEvent);
            }
        }

        #endregion

        #region Wall Feed
        /// <summary>
        /// Gets all the feed walls messages.
        /// </summary>
        /// <param name="language">The wallfeed's data language</param>
        /// <returns>The WallFeeds messages.</returns>
        public IEnumerable<WallFeed> GetAllWallFeeds(int language)
        {
            return zooDB.GetAllWallFeeds().Where(e => e.Language == language).ToArray();
        }

        /// <summary>
        /// Adds or Updates a feed wall message.
        /// </summary>
        /// <param name="feed">The wallfeed to add or update</param>
        public void UpdateWallFeed(WallFeed feed)
        {
            var wallFeeds = zooDB.GetAllWallFeeds();
            if (!wallFeeds.Contains(feed))
            {
                wallFeeds.Add(feed);
            }
        }

        /// <summary>
        /// Delete a feed wall message.
        /// </summary>
        /// <param name="id">The wallfeed's id to delete</param>
        public void DeleteWallFeed(int id)
        {
            var wallFeed = zooDB.GetAllWallFeeds().SingleOrDefault(wf => wf.Id == id);
            if (wallFeed != null)
            {
                zooDB.GetAllWallFeeds().Remove(wallFeed);
            }
        }

        #endregion

        #region General Info
        /// <summary>
        /// Gets the zoo's about info.
        /// </summary>
        /// <param name="language">The language the about info is in.</param>
        /// <returns>The zoo's about info.</returns>
        public IEnumerable<String> GetZooAboutInfo(int language)
        {
            return zooDB.GetGeneralInfo()
                .Where(ge => ge.Language == language)
                .Select(ge => ge.aboutUs)
                .ToArray();
        }

        /// <summary>
        /// Updates the zoo's about info.
        /// </summary>
        /// <param name="language">The language the about info is in.</param>
        /// <param name="info">The info to update.</param>
        public void UpdateZooAboutInfo(string info, int language)
        {
            var generalInfo = zooDB.GetGeneralInfo().SingleOrDefault(gi => gi.Language == language);

            if (generalInfo != null)
            {
                generalInfo.aboutUs = info;
            }
        }

        /// <summary>
        /// Gets the zoo's opening hours note.
        /// </summary>
        /// <param name="language">The language the note is in.</param>
        /// <returns>The zoo's openingHourNote.</returns>
        public IEnumerable<String> GetOpeningHourNote(int language)
        {
            return zooDB.GetGeneralInfo()
                .Where(ge => ge.Language == language)
                .Select(ge => ge.openingHoursNote)
                .ToArray();
        }

        /// <summary>
        /// Updates the zoo's OpeningHourNote.
        /// </summary>
        /// <param name="language">The language the note is in.</param>
        /// <param name="note">The note to update.</param>
        public void UpdateOpeningHourNote(string note, int language)
        {
            var generalInfo = zooDB.GetGeneralInfo().SingleOrDefault(gi => gi.Language == language);

            if (generalInfo != null)
            {
                generalInfo.openingHoursNote = note;
            }
        }

        /// <summary>
        /// Gets the zoo's contact info note.
        /// </summary>
        /// <param name="language">The language the note is in.</param>
        /// <returns>The zoo's ContactInfoNote.</returns>
        public IEnumerable<String> GetContactInfoNote(int language)
        {
            return zooDB.GetGeneralInfo()
                .Where(ge => ge.Language == language)
                .Select(ge => ge.contactInfoNote)
                .ToArray();
        }

        /// <summary>
        /// Updates the zoo's contact info note.
        /// </summary>
        /// <param name="language">The language the note is in.</param>
        /// <param name="note">The note to update.</param>
        public void UpdateContactInfoNote(string note, int language)
        {
            var generalInfo = zooDB.GetGeneralInfo().SingleOrDefault(gi => gi.Language == language);

            if (generalInfo != null)
            {
                generalInfo.contactInfoNote= note;
            }
        }
        #endregion

        



        #endregion

        public IEnumerable<RecurringEvent> GetAllRecurringEvents(int language)
        {
            return zooDB.GetAllRecuringEvents().Where(gr => gr.Language == language);
        }

        public void Dispose()
        {
            // zooDB.saveChanges();
        }
    }
}
