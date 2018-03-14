﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using Backend;
using Backend.Models;
using DAL;

namespace BL
{
    public class ZooContext : IDisposable 
    {
        private static IZooDB zooDB;

        // TODO:: Mark isTesting to false when the database it ready.
        public ZooContext(bool isTesting = true)
        {
            if (isTesting)
            {
                zooDB = zooDB ?? new DummyDB();
            }
            else
            {
                // TODO:: whenever the database will be ready - initialize it here.
            }
        }

        public static void CleanDb(bool isTesting = true)
        {
            if (isTesting)
            {
                zooDB = null;
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
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

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
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            Enclosure enc = zooDB.GetAllEnclosures().SingleOrDefault(e => e.Language == language && e.Id == id);

            if (enc == null)
            {
                throw new ArgumentException("Wrong input. enclosure id doesn't exists");
            }

            return enc;
        }

        /// <summary>
        /// Gets the enclosure by it's name.
        /// </summary>
        /// <param name="language">The enclosure's data language.</param>
        /// <param name="name">The enclosure's name.</param>
        /// <returns>The enclosure.</returns>
        public IEnumerable<Enclosure> GetEnclosureByName(string name, int language)
        {
            //TODO: should a wrong name return empty list or exception?
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            //var enc = zooDB.GetAllEnclosures().SingleOrDefault(e => e.Name == name);

            //if (enc == null)
            //{
            //    throw new HttpRequestException("Status code " + HttpStatusCode.NotFound + ", could not found the wanted enclosure.");
            //}

            return zooDB.GetAllEnclosures().Where(e => e.Language == language && e.Name.Contains(name));
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

        public IEnumerable<WallFeed> GetWallFeed(int language)
        {
            return zooDB.GetWallFeed().Where(e => e.Language == language).ToArray();
        }

        /// <summary>
        /// Gets the enclosure's recurring events by it's id.
        /// </summary>
        /// <param name="language">The enclosure's data language.</param>
        /// <param name="encId">The enclosure's id.</param>
        /// <returns>The enclosure's recurring events.</returns>
        public IEnumerable<RecurringEvent> GetRecurringEvents (int encId, int language)
        {
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            var enc = zooDB.GetAllEnclosures().SingleOrDefault(e => e.Id == encId);

            if(enc == null)
            {
                throw new ArgumentException("Wrong input. enclosure id doesn't exists");
            }

            return zooDB.GetAllRecuringEvents().Where(re => re.Language == language && re.EncId == encId).ToList();
        }

        /// <summary>
        /// Updates The enclosure.
        /// </summary>
        /// <param name="enclosures">The enclosures to update.</param>
        public void UpdateEnclosure(Enclosure enclosure)
        {
            //validate enclosure attributes
            //0. Exists.
            if (enclosure == default(Enclosure))
            {
                throw new ArgumentException("No enclosure given.");
            }

            //1. language
            if (!ValidLanguage(enclosure.Language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            //2. enclosure name
            if (String.IsNullOrWhiteSpace(enclosure.Name))
            {
                throw new ArgumentException("Wrong input. enclosure name is empty");
            }

            //TODO: add a check to latitude or longtitude out of the range of the zoo.

            var enclosures = zooDB.GetAllEnclosures();

            if (enclosure.Id == default(int)) //add a new enclosure
            {
                
                if (enclosures.Any(en => en.Name == enclosure.Name))
                {
                    throw new ArgumentException("Wrong input in adding enclosure. Name already exists");
                }
                
                enclosures.Add(enclosure);
            }
            else //update existing enclosure
            {
                Enclosure oldEnc = enclosures.SingleOrDefault(en => en.Id == enclosure.Id);

                //check that the enclosure exists
                if (oldEnc == null)
                {
                    throw new ArgumentException("Wrong input. Enclosure doesn't exits");
                }

                // check that if the name changed, it doesn't exits
                if (oldEnc.Name != enclosure.Name && enclosures.Any(en => en.Name == enclosure.Name))//The name changed
                {
                    throw new ArgumentException("Wrong input in updating enclosure. Name already exsits");
                }

                enclosures.Remove(oldEnc);
                enclosures.Add(enclosure);
            }
        }

        /// <summary>
        /// Delete The recurringEvent.
        /// </summary>
        /// <param name="id">The RecurringEvent's id to delete.</param>
        public void DeleteRecurringEvent(int id)
        {
            RecurringEvent recEvent = zooDB.GetAllRecuringEvents().SingleOrDefault(re => re.Id == id);

            if (recEvent == null)
            {
                throw new ArgumentException("Wrong input. RecurringEvent's id doesn't exists.");
            }

            zooDB.GetAllRecuringEvents().Remove(recEvent);
        }

        /// <summary>
        /// Delete The enclosure.
        /// </summary>
        /// <param name="id">The enclosure's id to delete.</param>
        public void DeleteEnclosure(int id)
        {
            Enclosure enclosure = zooDB.GetAllEnclosures().SingleOrDefault(e => e.Id == id);

            //Check that can delete the enclosure
            //1.enclosure exists
            if (enclosure == null)
            {
                throw new ArgumentException("Wrong input. enclosure ID doesn't exists.");
            }

            //2. exists animals
            if (zooDB.GetAllAnimals().Any(an => an.EncId == id))
            {
                throw new InvalidOperationException("Threre are animals that related to this enclosure");
            }

            //3. exists recurring events
            if (zooDB.GetAllRecuringEvents().Any(re => re.EncId == id))
            {
                throw new InvalidOperationException("Threre are recurring events that related to this enclosure");
            }

            zooDB.GetAllEnclosures().Remove(enclosure);
        }

        /// <summary>
        /// Gets the recurring events.
        /// </summary>
        /// <param name="language">The RecurringEvent's data language.</param>
        /// <returns>The RecurringEvents.</returns>
        public IEnumerable<RecurringEvent> GetAllRecurringEvents(int language)
        {
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            return zooDB.GetAllRecuringEvents().Where(gr => gr.Language == language);
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
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }
            return zooDB.GetAllAnimals().Where(a => a.Language == language).ToArray();
        }

        /// <summary>
        /// Gets animal by Id and language.
        /// </summary>
        /// <param name="language">The data's language</param>
        /// <param name="id">The animal's Id.</param>
        /// <returns>The animal.</returns>
        public Animal GetAnimalById(int id, int language)
        {
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }
            Animal an = zooDB.GetAllAnimals().SingleOrDefault(a => a.Language == language && a.Id == id);

            if (an == null)
            {
                throw new ArgumentException("Wrong input. animal id doesn't exsits");
            }

            return an;
        }

        /// <summary>
        /// Gets animal by Id and language.
        /// </summary>
        /// <param name="language">The data's language</param>
        /// <param name="name">The animal's name.</param>
        /// <returns>The animal.</returns>
        public IEnumerable<Animal> GetAnimalByName(string name, int language)
        {
            //TODO: should a wrong name return empty list or exception?
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            return zooDB.GetAllAnimals().Where(a => a.Language == language && a.Name.Contains(name));
        }

        /// <summary>
        /// Gets animals by enclosure Id and language.
        /// </summary>
        /// <param name="language">The data's language</param>
        /// <param name="encId">The enclosure's Id.</param>
        /// <returns>The animals in the enclosure.</returns>
        public IEnumerable<Animal> GetAnimalsByEnclosure(int encId, int language)
        {
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            //check if the enclosure exists with the wanted langauge
            if (GetAllEnclosures(language).SingleOrDefault(en => en.Id == encId) == null)
            {
                throw new ArgumentException("Wrong input. The enclosure doesn't exists");
            }

            return zooDB.GetAllAnimals().Where(a => a.Language == language && a.EncId == encId).ToList();
        }

        /// <summary>
        /// Updates the animal.
        /// </summary>
        /// <param name="animals">The animal to update.</param>
        public void UpdateAnimal(Animal animal)
        {
            //validate animal attributes
            //1. language
            if (!ValidLanguage(animal.Language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            //2. aniaml name
            if (String.IsNullOrWhiteSpace(animal.Name))
            {
                throw new ArgumentException("Wrong input. Animal name is empty or null");
            }

            //3. enclosure exists
            if (GetEnclosureById(animal.EncId, animal.Language) == null)
            {
                throw new ArgumentException("Wrong input. Enclosure id doesn't exists");
            }

            var animals = zooDB.GetAllAnimals();

            if (animal.Id == default(int)) //add a new aniaml
            {
                // check that the name doesn't exists
                if (animals.Any(an => an.Name == animal.Name))
                {
                    throw new ArgumentException("Wrong input in adding animal. Animal name already exists");
                }

                animals.Add(animal);
            }
            else // update existing animal.
            {
                Animal oldAnimal = animals.SingleOrDefault(an => an.Id == animal.Id);

                //check that the animal exists
                if (oldAnimal == null)
                {
                    throw new ArgumentException("Wrong input. Animal id does'nt exits");
                }

                // check that id the name changed, it doesn't exists.
                if (oldAnimal.Name != animal.Name && animals.Any(an => an.Name == animal.Name))
                {
                    throw new ArgumentException("Wrong input in updating animal. Animal name already exitst");
                }

                animals.Remove(oldAnimal);
                animals.Add(animal);
            }
        }

        /// <summary>
        /// Delete the animal.
        /// </summary>
        /// <param name="id">The animal's id to delete.</param>
        public void DeleteAnimal(int id)
        {
            Animal animal = zooDB.GetAllAnimals().SingleOrDefault(a => a.Id == id);
            if (animal == null)
            {
                throw new ArgumentException("Wrong input. Animal doesn't exists");
            }
            zooDB.GetAllAnimals().Remove(animal);
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
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            return zooDB.GetAllPrices().Where(p => p.Language == language).ToArray();
        }
        
        /// <summary>
        /// Adds or update the Price elements.
        /// </summary>
        /// <param name="price">The Price to add or update.</param>
        public void UpdatePrice(Price price)
        {
            //Validate the price attribute
            //0. Exists
            if (price == default(Price))
            {
                throw new ArgumentException("No price given.");
            }

            //1. check that the population is valid
            if (String.IsNullOrWhiteSpace(price.Population) || String.IsNullOrEmpty(price.Population))
            {
                throw new ArgumentException("Wrong input. The price population is empty or null");
            }
            
            //2. check that the price amount is valid
            if (price.PricePop < 0)
            {
                throw new ArgumentException("Wrong input, The price amount is lower than 0");
            }

            //3. check the language
            if (!ValidLanguage(price.Language))
            {
                throw new ArgumentException("Wrong Input. Wrong Language");
            }
            
            var prices = zooDB.GetAllPrices();

            if (price.Id == default(int)) //need to add the price.
            {
                if (prices.Any(p => p.Population == price.Population))
                {
                    throw new ArgumentException("Wrong input while adding price. Price population already exists.");
                }

                prices.Add(price);
            }
            else //update existing price
            {
                var oldPrice = prices.SingleOrDefault(p => p.Id == price.Id);

                //check that the price exists
                if (oldPrice == null)
                {
                    throw new ArgumentException("Wrong input. The price id doesn't exist");
                }

                //check that if the population changed, the new population doesn't exists.
                if (price.Population != oldPrice.Population && prices.Any(p => p.Population == price.Population))
                {
                    throw new ArgumentException("Wrong input while updating price. Price population already exists");
                }

                prices.Remove(oldPrice);
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

            if (price == null)
            {
                throw new ArgumentException("Wrong input. Price doesn't exists.");
            }

            zooDB.GetAllPrices().Remove(price);
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
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            return zooDB.GetAllOpeningHours().Where(oh => oh.Language == language).ToArray();
        }

        /// <summary>
        /// Adds or update the OpeningHour element.
        /// </summary>
        /// <param name="OpeningHour">The OpeningHour element to add or update.</param>
        public void UpdateOpeningHour(OpeningHour openingHour)
        {
            //validate opening hour attributs
            //0. Exists
            if (openingHour == default(OpeningHour))
            {
                throw new ArgumentException("No OpeningHour was given");
            }

            //1. check the day
            if (String.IsNullOrWhiteSpace(openingHour.Day) || String.IsNullOrEmpty(openingHour.Day))
            {
                throw new ArgumentException("Wrong input. The day is empty or null");
            }

            //2. check the opening time
            if (!ValidHour(openingHour.StartHour, openingHour.StartMin))
            {
                throw new ArgumentException("Wrong input. Wrong opening time");
            }

            //3. check the closing time
            if (!ValidHour(openingHour.EndHour, openingHour.EndMin))
            {
                throw new ArgumentException("Wrong input. Wrong closing time");
            }

            //4. check that the end is after the open
            if (openingHour.StartHour > openingHour.EndHour || (openingHour.StartHour == openingHour.EndHour && openingHour.StartMin > openingHour.EndMin))
            {
                throw new ArgumentException("Wrong input. The start time is later than the end time.");
            }

            //5. check the language
            if (!ValidLanguage(openingHour.Language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            var openingHours = zooDB.GetAllOpeningHours();

            if (openingHour.Id == default(int)) //add a new opening hour
            {
                if (openingHours.Any(oh => oh.Day == openingHour.Day))
                {
                    throw new ArgumentException("Wrong input while adding Opening hour. The day of the opening hour is already exsists");
                }

                openingHours.Add(openingHour);
            }
            else //update exsist opening hour
            {
                OpeningHour oldHour = openingHours.SingleOrDefault(oh => oh.Id == openingHour.Id);
                
                //check that the id exists
                if (oldHour == null)
                {
                    throw new ArgumentException("Wrong input. The opening hour id doesn't exists.");
                }

                //check that if the day changed than the new day doesnt exists.
                if (oldHour.Day != openingHour.Day && openingHours.Any(oh => oh.Day == openingHour.Day))
                {
                    throw new ArgumentException("Wrong input while updating Opening hour. The Opening hour day already exists");
                }

                openingHours.Remove(oldHour);
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

            if (openingHour == null)
            {
                throw new ArgumentException("Wrong input. Opening hour id doesn't exsists.");
            }
            zooDB.GetAllOpeningHours().Remove(openingHour);
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
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            return zooDB.GetAllContactInfos().Where(ci => ci.Language == language).ToArray();
        }

        /// <summary>
        /// Adds or update the ContactInfo element.
        /// </summary>
        /// <param name="contactInfo">The ContactInfo element to add or update.</param>
        public void UpdateContactInfo(ContactInfo contactInfo)
        {
            //validate contact info attributs
            //0. Exists
            if (contactInfo == default(ContactInfo))
            {
                throw new ArgumentException("No ContactInfo was given");
            } 

            //1. check that the address is valid
            if (String.IsNullOrWhiteSpace(contactInfo.Address) || String.IsNullOrEmpty(contactInfo.Address))
            {
                throw new ArgumentException("Wrong input. ContactInfo's address is empty or null");
            }

            //2. check that the via is valid
            if (String.IsNullOrWhiteSpace(contactInfo.Via) || String.IsNullOrEmpty(contactInfo.Via))
            {
                throw new ArgumentException("Wrong input. ContactInfo's via is empty or null");
            }

            //3. check the language
            if (!ValidLanguage(contactInfo.Language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            var contactInfos = zooDB.GetAllContactInfos();

            if (contactInfo.Id == default(int)) // add a new contact info
            {
                //check that the address and via doesn't exists
                if (contactInfos.Any(ci => ci.Via == contactInfo.Via && ci.Address == contactInfo.Address))
                {
                    throw new ArgumentException("Wrong input while adding contactInfo. Contact info address and Via already exists.");
                }

                contactInfos.Add(contactInfo);
            }
            else //update existing contact info
            {
                ContactInfo oldContact = contactInfos.SingleOrDefault(ci => ci.Id == contactInfo.Id);

                //check that the contact info exists
                if (oldContact == null)
                {
                    throw new ArgumentException("Wrong input. The ContactInfo's id doesn't exists");
                }

                //check that id the via or address change there isn't exsisting via and address
                if ((oldContact.Via != contactInfo.Via || oldContact.Address != contactInfo.Address) && contactInfos.Any(ci => ci.Address == contactInfo.Address && ci.Via == contactInfo.Via))
                {
                    throw new ArgumentException("Wrong input while updating contactInfo. Contact info address and Via already exists.");
                }

                contactInfos.Remove(oldContact);
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

            if (contactInfo == null)
            {
                throw new ArgumentException("Wrong input. ContactInfo id doesn't exists");
            }

            zooDB.GetAllContactInfos().Remove(contactInfo);
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
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

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
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            if (DateTime.Compare(endDate,startDate) <= 0)
            {
                throw new ArgumentException("Wrong input. the end date is sooner than the start date");
            }

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
            //validate SpecialEvent attributs
            //0. Exists
            if (specialEvent == default(SpecialEvent))
            {
                throw new ArgumentException("No SpecialEvent was given");
            }

            //1. check that the dates are good
            if (DateTime.Compare(specialEvent.EndDate,specialEvent.StartDate) <=0)
            {
                throw new ArgumentException("Wrong input. The end date is earlier than the start date.");
            }

            //2. check the language
            if (!ValidLanguage(specialEvent.Language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            var specialEvents = zooDB.GetAllSpecialEvents();

            if (specialEvent.Id == default(int)) //add a new special event
            {
                //check that the description doesn't exists.
                if (specialEvents.Any(sp => sp.Description == specialEvent.Description))
                {
                    throw new ArgumentException("Wrong input while adding a SpecialEvent. The SpecialEvent description already exists");
                }

                specialEvents.Add(specialEvent);
            }
            else //update existing SpecialEvent
            {
                var oldEvent = specialEvents.SingleOrDefault(se => se.Id == specialEvent.Id);

                //check that the event exists.
                if (oldEvent == null)
                {
                    throw new ArgumentException("Wrong input. The SpecialEvent's id doesn't exists");
                }

                //check that if the description changed than it doesn't already exists
                if (oldEvent.Description != specialEvent.Description && specialEvents.Any(se => se.Description == specialEvent.Description))
                {
                    throw new ArgumentException("Wrong input While updating SpecialEvent. The SpecialEvent descroption already exists.");
                }

                specialEvents.Remove(oldEvent);
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

            if (specialEvent == null)
            {
                throw new ArgumentException("Wrong input. SpecialEvent's id doesn't exists");
            }
            zooDB.GetAllSpecialEvents().Remove(specialEvent);
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
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            return zooDB.GetAllWallFeeds().Where(e => e.Language == language).ToArray();
        }

        /// <summary>
        /// Adds or Updates a feed wall message.
        /// </summary>
        /// <param name="feed">The wallfeed to add or update</param>
        public void UpdateWallFeed(WallFeed feed)
        {
            //validate WallFeed attributs
            //0. Exists
            if (feed == default(WallFeed))
            {
                throw new ArgumentException("No SpecialEvent was given");
            }

            //1. valid creation date
            if (DateTime.Compare(DateTime.Today, feed.Created) < 0)
            {
                throw new ArgumentException("Wrong input. The creation date can't be later than today.");
            }

            //2. check the info
            if (String.IsNullOrWhiteSpace(feed.Info) || String.IsNullOrEmpty(feed.Info))
            {
                throw new ArgumentException("Wrong input. The info is null or white space");
            }

            //3. check the language
            if (!ValidLanguage(feed.Language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            var wallFeeds = zooDB.GetAllWallFeeds();

            if (feed.Id == default(int)) //add new feed wall
            {
                //check that the info doesn't exists
                if (wallFeeds.Any(wf => wf.Info == feed.Info))
                {
                    throw new ArgumentException("Wrong input while adding WallFeed. The WallFeed info is already exists.");
                }

                wallFeeds.Add(feed);
            }
            else //update a feed wall
            {
                WallFeed oldFeed = wallFeeds.SingleOrDefault(wf => wf.Id == feed.Id);

                //check that the wall feed exists
                if (oldFeed == null)
                {
                    throw new ArgumentException("Wrong input. The WallFeed's id doesn't exists");
                }

                //check that if the info changed than it doesn't already exits
                if (oldFeed.Info != feed.Info && wallFeeds.Any(wf => wf.Info == feed.Info))
                {
                    throw new ArgumentException("Wrong input while updating WallFeed. The WallFeed Info already exists");
                }

                wallFeeds.Remove(oldFeed);
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

            if (wallFeed == null)
            {
                throw new ArgumentException("Wrong input. WallFeed's id doesn't exists");
            }

            zooDB.GetAllWallFeeds().Remove(wallFeed);
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
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

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
            //validate the AboutUs attributs
            //1. validate info
            if (String.IsNullOrWhiteSpace(info) || String.IsNullOrEmpty(info))
            {
                throw new ArgumentException("Wrong input. The info is empty or null");
            }

            //2. validate language
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            var generalInfo = zooDB.GetGeneralInfo().SingleOrDefault(gi => gi.Language == language);

            generalInfo.aboutUs = info;
        }

        /// <summary>
        /// Gets the zoo's opening hours note.
        /// </summary>
        /// <param name="language">The language the note is in.</param>
        /// <returns>The zoo's openingHourNote.</returns>
        public IEnumerable<String> GetOpeningHourNote(int language)
        {
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

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
            //validate the OpeningHourNote attributs
            //1. validate note
            if (String.IsNullOrWhiteSpace(note))
            {
                throw new ArgumentException("Wrong input. The note is empty or null");
            }

            //2. validate language
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            var generalInfo = zooDB.GetGeneralInfo().SingleOrDefault(gi => gi.Language == language);

            generalInfo.openingHoursNote = note;
        }

        /// <summary>
        /// Gets the zoo's contact info note.
        /// </summary>
        /// <param name="language">The language the note is in.</param>
        /// <returns>The zoo's ContactInfoNote.</returns>
        public IEnumerable<String> GetContactInfoNote(int language)
        {
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

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
            //validate the ContactInfoNote attributs
            //1. validate note
            if (String.IsNullOrWhiteSpace(note))
            {
                throw new ArgumentException("Wrong input. The note is empty spaces or null");
            }

            //2. validate language
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            var generalInfo = zooDB.GetGeneralInfo().SingleOrDefault(gi => gi.Language == language);

            generalInfo.contactInfoNote= note;
        }
        #endregion

        #endregion

        #region Validate functions

        private bool ValidLanguage(int language)
        {
            return Enum.IsDefined(typeof(Languages), language);
        }

        private bool ValidHour(int hour, int min)
        {
            return hour > 0 && hour < 24 && Enum.IsDefined(typeof(AvailableMinutes), min);
        }

        #endregion

        public void Dispose()
        {
            // zooDB.saveChanges();
        }
    }
}
