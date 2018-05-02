using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Security.Cryptography;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Web;
using DAL;
using DAL.Models;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace BL
{
    public class ZooContext : IDisposable 
    {
        private IZooDB zooDB;

        public ZooContext(bool isTesting = true)
        {
            try
            {
                if (isTesting)
                {
                    zooDB = zooDB ?? DummyDB.GetInstance();
                }
                else
                {
                    zooDB = new NegevZooDBEntities();
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
                throw new Exception("Could not connect to the database");
            }
        }

        public ZooContext(IZooDB db)
        {
            zooDB = db;
        }

        #region Enclosure

        /// <summary>
        /// Gets the enclosures results.
        /// </summary>
        /// <param name="language">The enclosure's data language.</param>
        /// <returns>The enclosures.</returns>
        public IEnumerable<EnclosureResult> GetAllEnclosureResults(int language)
        {
            //validate the language
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            //pull all the data
            var enclosures          = zooDB.GetAllEnclosures().ToArray();
            var enclosureDetails    = zooDB.GetAllEnclosureDetails().Where(e => e.language == language).ToArray();
            var recEvents           = zooDB.GetAllRecuringEvents().ToArray();
            var encVideos           = zooDB.GetAllEnclosureVideos().ToArray();
            var encPicture          = zooDB.GetAllEnclosurePictures().ToArray();

            //create RecuringEventResults to the application
            var recEventsDet = new List<RecurringEventsResult>();
            foreach(RecurringEvent rec in recEvents)
            {
                recEventsDet.Add(new RecurringEventsResult
                {
                    Id          = rec.id,
                    Title       = rec.title,
                    Description = rec.description,
                    EnclosureId = rec.enclosureId,
                    StartTime   = Convert.ToInt64(rec.startTime.TotalMilliseconds * (rec.day%10)),
                    EndTime     = Convert.ToInt64(rec.endTime.TotalMilliseconds * (rec.day % 10)),
                    Language    = rec.language
                });
            }

            //create EnclosureResults from all the data of the enclosures
            var enclosureResults = from e in enclosures
                                   join ed in enclosureDetails on e.id equals ed.encId
                                   join vid in encVideos on e.id equals vid.enclosureId into encVid
                                   join pic in encPicture on e.id equals pic.enclosureId into encPic
                                   join eve in recEventsDet on e.id equals eve.EnclosureId into recEve
                                   select new EnclosureResult
                                   {
                                       Id                   = e.id,
                                       Language             = ed.language,
                                       MarkerIconUrl        = e.markerIconUrl,
                                       MarkerLatitude       = e.markerLatitude,
                                       MarkerLongtitude     = e.markerLongitude,
                                       PictureUrl           = e.pictureUrl,
                                       Name                 = ed.name,
                                       Story                = ed.story,
                                       Videos               = encVid.Where(ev => ev.enclosureId == e.id).ToArray(),
                                       Pictures             = encPic.Where(ep => ep.enclosureId == e.id).ToArray(),
                                       RecEvents            = recEve.Where(re => re.EnclosureId == e.id).ToArray()
                                   };

            return enclosureResults.ToArray();
        }

        /// <summary>
        /// Gets the enclosure by id.
        /// </summary>
        /// <param name="language">The enclosure's data language.</param>
        /// <param name="id">The enclosure's id.</param>
        /// <returns>The enclosure.</returns>
        /// 
        public EnclosureResult GetEnclosureById(int id, int language)
        {
            //validate atrributes
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            Enclosure enc = zooDB.GetAllEnclosures().SingleOrDefault(e => e.id == id);
            
            //check that the enclosure exists
            if (enc == null)
            {
                throw new ArgumentException("Wrong input. enclosure id doesn't exists");
            }

            EnclosureDetail details = zooDB.GetAllEnclosureDetails().SingleOrDefault(e => e.encId == id && e.language == language);

            //in case that there isn't data in the wanted language than taking the hebrew data.
            if (details == null)
            {
                var hebrewLang = GetHebewLanguage();
                details = zooDB.GetAllEnclosureDetails().SingleOrDefault(e => e.encId == id && e.language == hebrewLang);
            }

            var enclosureResult = new EnclosureResult
            {
                Id = enc.id,
                Name = details?.name,
                Story = details?.story,
                MarkerLatitude = enc.markerLatitude,
                MarkerLongtitude = enc.markerLongitude,
                MarkerIconUrl = enc.markerIconUrl,
                PictureUrl = enc.pictureUrl,
                Language = details == null ? GetHebewLanguage() : details.language 
            };

            return enclosureResult; 
        }
        
        /// <summary>
        /// Gets the enclosure by it's name.
        /// </summary>
        /// <param name="language">The enclosure's data language.</param>
        /// <param name="name">The enclosure's name.</param>
        /// <returns>The enclosure.</returns>
        public IEnumerable<EnclosureResult> GetEnclosureByName(string name, int language)
        {
            var enclosureResults = new List<EnclosureResult>();

            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            var encDetails = zooDB.GetAllEnclosureDetails().Where(e => e.name.Contains(name));

            // it there are details in the name and language
            if (encDetails != null)
            {
                foreach(EnclosureDetail details in encDetails)
                {
                    enclosureResults.Add(GetEnclosureById((int)details.encId ,language));
                }

            }

            return enclosureResults;
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
            return zooDB.GetAllEnclosures().SingleOrDefault(e => /*e.language == language &&*/
                                                            (e.markerLongitude <= longtitud + 5 && e.markerLongitude >= longtitud - 5) &&
                                                            (e.markerLatitude <= latitude + 5 && e.markerLatitude >= latitude - 5) );
        }

        /// <summary>
        /// Gets the enclosures types results.
        /// </summary>
        /// <returns>The enclosures types .</returns>
        public IEnumerable<Enclosure> GetAllEnclosures()
        {
            return zooDB.GetAllEnclosures();
        }

        /// <summary>
        /// Gets the enclosure details by the enclosure type id.
        /// </summary>
        /// <param name="encId">The enclosure's type id.</param>
        /// <returns>The enclosure details in all the languages.</returns>
        public IEnumerable<EnclosureDetail> GetEnclosureDetailsById(int encId)
        {
            return zooDB.GetAllEnclosureDetails().Where(e => e.encId == encId);
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

            var enc = zooDB.GetAllEnclosures().SingleOrDefault(e => e.id == encId);

            if(enc == null)
            {
                throw new ArgumentException("Wrong input. enclosure id doesn't exists");
            }

            return zooDB.GetAllRecuringEvents().Where(re => re.language == language && re.enclosureId == encId).ToList();
        }
   
        /// <summary>
        /// Gets the enclosure's pictures by it's id.
        /// </summary>
        /// <param name="encId">The enclosure's id.</param>
        /// <returns>The enclosure's pictures events.</returns>
        public IEnumerable<EnclosurePicture> GetEnclosurePicturesById(int encId)
        {
            //check that the enclosure exists
            if (!GetAllEnclosures().Any(e => e.id == encId))
            {
                throw new ArgumentException("Wrong input. The enclosure doesn't exists");
            }

            return zooDB.GetAllEnclosurePictures().Where(e => e.enclosureId == encId);
        }

        /// <summary>
        /// Gets the enclosure's videos by it's id.
        /// </summary>
        /// <param name="encId">The enclosure's id.</param>
        /// <returns>The enclosure's videos events.</returns>
        public IEnumerable<YoutubeVideoUrl> GetEnclosureVideosById(int encId)
        {
            //check that the enclosure exists
            if (!GetAllEnclosures().Any(e => e.id == encId))
            {
                throw new ArgumentException("Wrong input. The enclosure doesn't exists");
            }

            return zooDB.GetAllEnclosureVideos().Where(e => e.enclosureId == encId);
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

            return zooDB.GetAllRecuringEvents().Where(gr => gr.language == language);
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

            //1. enclosure name
            if (String.IsNullOrWhiteSpace(enclosure.name))
            {
                throw new ArgumentException("Wrong input. enclosure name is null or white space");
            }

            //TODO: add a check to latitude or longtitude out of the range of the zoo.

            var enclosures = zooDB.GetAllEnclosures();

            if (enclosure.id == default(int)) //add a new enclosure
            {
                if (enclosures.Any(en => en.name == enclosure.name))
                {
                    throw new ArgumentException("Wrong input while adding enclosure. Name already exists");
                }
                
                enclosures.Add(enclosure);
            }
            else //update existing enclosure
            {
                Enclosure oldEnc = enclosures.SingleOrDefault(en => en.id == enclosure.id);

                //check that the enclosure exists
                if (oldEnc == null)
                {
                    throw new ArgumentException("Wrong input. Enclosure id doesn't exits");
                }

                // check that if the name changed, it doesn't exits
                if (oldEnc.name != enclosure.name && enclosures.Any(en => en.name == enclosure.name))//The name changed
                {
                    throw new ArgumentException("Wrong input while updating enclosure. Name already exsits");
                }

                //enclosure.id = oldEnc.id;
                oldEnc.markerIconUrl = enclosure.markerIconUrl;
                oldEnc.markerLatitude = enclosure.markerLatitude;
                oldEnc.markerLongitude = enclosure.markerLongitude;
                oldEnc.name = enclosure.name;
                oldEnc.pictureUrl = enclosure.pictureUrl;
            }
        }

        /// <summary>
        /// Updates or adds The enclosure details.
        /// </summary>
        /// <param name="enclosureDetail">The enclosureDetails to update.</param>
        public void UpdateEnclosureDetails(EnclosureDetail enclosureDetail)
        {
            //validate enclosure details attributes
            //0. Exists.
            if (enclosureDetail == default(EnclosureDetail) || enclosureDetail.encId == default(int))
            {
                throw new ArgumentException("No enclosure given.");
            }

            //1. validate language
            if (!ValidLanguage((int)enclosureDetail.language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            //2. enclosure detail name
            if (String.IsNullOrWhiteSpace(enclosureDetail.name))
            {
                throw new ArgumentException("Wrong input. enclosure detail name is empty or white space");
            }
            
            //3. check that the enclosure id exists
            if (!GetAllEnclosures().Any(e => e.id == enclosureDetail.encId))
            {
                throw new ArgumentException("Wrong input. Enclosure detail id doesn't exists.");
            }

            var enclosuresDetails = zooDB.GetAllEnclosureDetails();

            EnclosureDetail oldEnc = enclosuresDetails.SingleOrDefault(en => en.encId == enclosureDetail.encId && en.language == enclosureDetail.language);

            if (oldEnc == null) //add a new enclosure details
            {
                if (enclosuresDetails.Any(ed => ed.name == enclosureDetail.name))
                {
                    throw new ArgumentException("Wrong input. The name of the enclosure alreay exists.");
                }
                enclosuresDetails.Add(enclosureDetail);
            }
            else //update an exists enclosure detail
            //get the existing enclosure detail
            {
                // check that if the name changed, it doesn't exits
                if (oldEnc.name != enclosureDetail.name && enclosuresDetails.Any(en => en.name == enclosureDetail.name))//The name changed
                {
                    throw new ArgumentException("Wrong input in updating enclosure. Name already exsits");
                }

                oldEnc.language = enclosureDetail.language;
                oldEnc.name = enclosureDetail.name;
                oldEnc.story = enclosureDetail.story;
            }
        }
        
        public IEnumerable<EnclosurePicture> AddEnclosurePictures(int enclosureId, JArray uploadedPictures)
        {
            // Get the enclosure.
            var enclosure = this.GetAllEnclosures().SingleOrDefault(e => e.id == enclosureId);

            // If no such enclosure exists, throw error.
            if (enclosure == default(Enclosure))
            {
                throw new ArgumentException("No enclosure with such enclosure Id exists.");
            }

            var pictures = uploadedPictures.Select(up => new EnclosurePicture { enclosureId = enclosureId, pictureUrl = up.Value<String>() });

            if (pictures.Any(p => String.IsNullOrWhiteSpace(p.pictureUrl)))
            {
                throw new ArgumentException("Wrong input. The url is empty or white spaces");
            }

            if (pictures.Any(up => up.enclosureId != enclosureId))
            {
                throw new InvalidOperationException("Cannot add pictures to more than one enclosure at a time.");
            }

            var enclosurePictures = zooDB.GetAllEnclosurePictures();

            enclosurePictures.AddRange(pictures);

            zooDB.SaveChanges();

            return pictures;
        }

        /// <summary>
        /// Updates or adds The enclosure video.
        /// </summary>
        /// <param name="enclosureVideo">The enclosures to update.</param>
        public YoutubeVideoUrl UpdateEnclosureVideo(YoutubeVideoUrl enclosureVideo)
        {
            //validate attributes
            //0.Exists
            if (enclosureVideo == default(YoutubeVideoUrl))
            {
                throw new ArgumentException("No video given");
            }
            
            //1. check that the enclosure exists
            if (!GetAllEnclosures().Any(e => e.id == enclosureVideo.enclosureId))
            {
                throw new ArgumentException("Wrong input. Enclosure doesn't exists");
            }

            //2. check the url
            if (String.IsNullOrWhiteSpace(enclosureVideo.videoUrl))
            {
                throw new ArgumentException("Wrong input. The url is empty or white spaces");
            }

            var allEnclosureVideos = zooDB.GetAllEnclosureVideos();

            if (enclosureVideo.id == default(int)) //add video
            {
                if (allEnclosureVideos.Any(v => v.videoUrl == enclosureVideo.videoUrl))
                {
                    throw new ArgumentException("Wrong input while adding enclosure video. The enclosure vido url already exists");
                }

                allEnclosureVideos.Add(enclosureVideo);
            }
            else //update video
            {
                var oldVideo = allEnclosureVideos.SingleOrDefault(v => v.id == enclosureVideo.id);

                if (oldVideo == null)
                {
                    throw new ArgumentException("Wrong input. Video doesn't exists");
                }

                if (oldVideo.videoUrl != enclosureVideo.videoUrl && allEnclosureVideos.Any(v => v.videoUrl == enclosureVideo.videoUrl))
                {
                    throw new ArgumentException("Wrong input while updating enclosure video. The enclosure video url already exists");
                }

                oldVideo.videoUrl = enclosureVideo.videoUrl;
                oldVideo.enclosureId = enclosureVideo.enclosureId;
            }

            zooDB.SaveChanges();

            return enclosureVideo;
        }
        
        /// <summary>
        /// Adds or updates a recurring event element.
        /// </summary>
        /// <param name="recEvent">The RecurringEvent element to update or add.</param>
        public void UpdateRecurringEvent(RecurringEvent recEvent)
        {
            //validate attributes
            //0.Exists
            if (recEvent == default(RecurringEvent))
            {
                throw new ArgumentException("No RecurringEvent was given");
            }

            //1. validate language
            if (!ValidLanguage((int)recEvent.language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            //2. check that the enclosure exists
            if (!GetAllEnclosures().Any(e => e.id == recEvent.enclosureId))
            {
                throw new ArgumentException("Wrong input. Enclosure doesn't exists");
            }

            //3. check the description
            if (String.IsNullOrWhiteSpace(recEvent.description))
            {
                throw new ArgumentException("Wrong input. The descrioption is null or white spaces");
            }

            //4. check the day
            if (Enum.ToObject(typeof(Days), recEvent.day) == null || recEvent.day / 10 != (recEvent.language - 1))
            {
                throw new ArgumentException("Wrong input. The day is not defined");
            }

            //5. check the time
            if (recEvent.endTime.Subtract(recEvent.startTime) <= TimeSpan.Zero)
            {
                throw new ArgumentException("Wrong input. The start time is bigger or equal to the end time");
            }

            var allRecurringEvents = zooDB.GetAllRecuringEvents();

            var enclosureRecurringEvents = allRecurringEvents.Where(re => re.enclosureId == recEvent.enclosureId).ToList();

            //TODO: Add Notification!
            if (recEvent.id == default(int)) //add recurring event
            {
                //check that there isn't other Recurring event to this enclosure in the same time.
                if (enclosureRecurringEvents.Any(re => ValidateTime(re, recEvent)))
                {
                    throw new ArgumentException("Wrong input while adding recurring event. There is another recurring event in the same time");
                }

                allRecurringEvents.Add(recEvent);
            }
            else //update RecurringEvent
            {
                var oldRecEvent = allRecurringEvents.SingleOrDefault(re => re.id == recEvent.id);

                if (oldRecEvent == null)
                {
                    throw new ArgumentException("Wrong input. RecurringEvent doesn't exists");
                }

                if (allRecurringEvents.Any(re => re.enclosureId == recEvent.enclosureId && ValidateTime(re, recEvent)))
                {
                    throw new ArgumentException("Wrong input while updating enclosure video. The enclosure vido url already exists");
                }

                oldRecEvent.enclosureId = recEvent.enclosureId;
                oldRecEvent.day         = recEvent.day;
                oldRecEvent.description = recEvent.description;
                oldRecEvent.startTime   = recEvent.startTime;
                oldRecEvent.endTime     = recEvent.endTime;
            }
            
        }

        /// <summary>
        /// Delete The enclosure.
        /// </summary>
        /// <param name="id">The enclosure's id to delete.</param>
        public void DeleteEnclosure(int id)
        {
            Enclosure enclosure = zooDB.GetAllEnclosures().SingleOrDefault(e => e.id == id);

            //Check that can delete the enclosure
            //1.enclosure exists
            if (enclosure == null)
            {
                throw new ArgumentException("Wrong input. enclosure ID doesn't exists.");
            }

            //2. exists animals
            if (zooDB.GetAllAnimals().Any(an => an.enclosureId == id))
            {
                throw new InvalidOperationException("Threre are animals that related to this enclosure");
            }

            //3. exists recurring events
            if (zooDB.GetAllRecuringEvents().Any(re => re.enclosureId == id))
            {
                throw new InvalidOperationException("Threre are recurring events that related to this enclosure");
            }

            var toRemove = zooDB.GetAllEnclosureDetails().Where(ed => ed.encId == enclosure.id).ToList();

            zooDB.GetAllEnclosureDetails().RemoveRange(toRemove);
            zooDB.GetAllEnclosures().Remove(enclosure);
        }

        /// <summary>
        /// Delete The enclosure picture.
        /// </summary>
        /// <param name="enclosurePictureId">The EnclosurePicture's id to delete.</param>
        public void DeleteEnclosurePicture(int enclosureId, int enclosurePictureId)
        {
            var allEnclosurePictures = zooDB.GetAllEnclosurePictures();
            
            //check that the enclosure picture exists
            var enclosurePicture = allEnclosurePictures.SingleOrDefault(e => e.id == enclosurePictureId);

            if (enclosurePicture == null){
                throw new ArgumentException("Wrong input. The enclsure picture doesn't exists");
            }

            if (enclosurePicture.enclosureId != enclosureId)
            {
                throw new InvalidOperationException("Cannot delete a picture of another enclosure.");
            }

            allEnclosurePictures.Remove(enclosurePicture);
        }

        /// <summary>
        /// Delete The enclosure video.
        /// </summary>
        /// <param name="enclosureVideoId">The EnclosureVideo's id to delete.</param>
        public void DeleteEnclosureVideo(int enclosureId, int enclosureVideoId)
        {
            //check that the enclosure exists
            var enclosureVideo = zooDB.GetAllEnclosureVideos().SingleOrDefault(e => e.id == enclosureVideoId && e.enclosureId == enclosureId);

            if (enclosureVideo == null)
            {
                throw new ArgumentException("Wrong input. The video doesn't exist or does not belong to the enclosure");
            }

            zooDB.GetAllEnclosureVideos().Remove(enclosureVideo);
        }
        
        /// <summary>
        /// Delete The recurringEvent.
        /// </summary>
        /// <param name="id">The RecurringEvent's id to delete.</param>
        public void DeleteRecurringEvent(int enclosureId, int recurringEventId)
        {
            RecurringEvent recEvent = zooDB.GetAllRecuringEvents().SingleOrDefault(re => re.id == recurringEventId && re.enclosureId == enclosureId);

            if (recEvent == null)
            {
                throw new ArgumentException("Wrong input. RecurringEvent's id doesn't exists.");
            }

            zooDB.GetAllRecuringEvents().Remove(recEvent);
        }
        
        #endregion

        #region Animals
        
        /// <summary>
        /// Gets all the animals results.
        /// </summary>
        /// <param name="language">The animal's data language.</param>
        /// <returns>All the AnimalResults with the given language.</returns>
        public IEnumerable<AnimalResult> GetAnimalsResults(int language)
        {
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }
            var animals = zooDB.GetAllAnimals();
            var animalsDetails = zooDB.GetAllAnimalsDetails();

            var animalResults = from a in animals
                                join ad in animalsDetails on new { a.id, language = (long)language } equals new { id = ad.animalId, ad.language }
                                select new AnimalResult
                                {
                                    Id              = a.id,
                                    Name            = ad.name,
                                    Story           = ad.story,
                                    EncId           = a.enclosureId,
                                    Category        = ad.category,
                                    Series          = ad.series,
                                    Family          = ad.family,
                                    Distribution     = ad.distribution,
                                    Reproduction    = ad.reproduction,
                                    Food            = ad.food,
                                    Preservation    = a.preservation,
                                    PictureUrl      = a.pictureUrl,
                                    Language        = ad.language
                                };

            return animalResults.ToArray();
        }

        /// <summary>
        /// This method gets all the AnimalResults that have a speicsial stories.
        /// </summary>
        /// <param name="language">The data's language</param>
        /// <returns>All the AnimalResults that have a special story in the given language.</returns>
        public IEnumerable<AnimalResult> GetAnimalResultsWithStory(int language)
        {
            return GetAnimalsResults(language).Where(ar => !String.IsNullOrWhiteSpace(ar.Story));
        }

        /// <summary>
        /// Gets animal by Id and language.
        /// </summary>
        /// <param name="id">The animal's Id.</param>
        /// <param name="language">The data's language</param>
        /// <returns>The animal.</returns>
        public AnimalResult GetAnimalById(int id, int language)
        {
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            Animal an = zooDB.GetAllAnimals().SingleOrDefault(a => a.id == id);

            if (an == null)
            {
                throw new ArgumentException("Wrong input. animal id doesn't exsits");
            }

            AnimalDetail details = zooDB.GetAllAnimalsDetails().SingleOrDefault(ad => ad.animalId == id && ad.language == language);

            //in case that there isn't data in the wanted language than taking the hebrew data.
            if (details == null)
            {
                var hebrewLang = GetHebewLanguage();
                details = zooDB.GetAllAnimalsDetails().SingleOrDefault(ad => ad.animalId == id && ad.language == hebrewLang);
            }

            var animalResult = new AnimalResult
            {
                Id = id,
                Name = details?.name,
                Story = details?.story,
                EncId = an.enclosureId,
                Category = details?.category,
                Distribution = details?.distribution,
                Family = details?.family,
                Food = details?.food,
                Preservation = an.preservation,
                Reproduction = details?.reproduction,
                Series = details?.series,
                PictureUrl = an.pictureUrl,
                Language = details == null ? GetHebewLanguage() : details.language
            };

            return animalResult;
        }
        
        /// <summary>
        /// Gets animals by enclosure Id and language.
        /// </summary>
        /// <param name="encId">The enclosure's Id.</param>
        /// <param name="language">The data's language</param>
        /// <returns>The AnimalResults of animals that are in the enclosure.</returns>
        public IEnumerable<AnimalResult> GetAnimalResultByEnclosure(long encId, int language)
        {
            //validate the attributes
            //0. check the language
            if (!ValidLanguage((int)language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            //1. check if the enclosure exists
            if (GetAllEnclosures().SingleOrDefault(en => en.id == encId) == null)
            {
                throw new ArgumentException("Wrong input. The enclosure doesn't exists");
            }

            //get all the animals in the enclosure.
            IEnumerable<Animal> allanimals = GetAllAnimals().Where(a => a.enclosureId == encId).ToArray();

            //initiates the answer.
            List<AnimalResult> animalsResult = new List<AnimalResult>();

            foreach (Animal an in allanimals)
            {
                //foreaach animal get it's AnimalResult by id (if it doesn't exists returns in hebrew.
                animalsResult.Add(GetAnimalById((int)an.id, (int)language));
            }

            return animalsResult;
        }

        /// <summary>
        /// Gets all the animals that their name conatins the given name in the wanted language.
        /// </summary>
        /// <param name="language">The data's language</param>
        /// <param name="name">The animal's name.</param>
        /// <returns>The AnimalResult.</returns>
        public IEnumerable<AnimalResult> GetAnimalByName(string name, int language)
        {
            var animalResult = new List<AnimalResult>();

            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            var anDetails = zooDB.GetAllAnimalsDetails().Where(ad => ad.name.Contains(name) && ad.language == language);

            // it there are details in the name and language
            if (anDetails != null)
            {
                foreach (AnimalDetail details in anDetails)
                {
                    animalResult.Add(GetAnimalById((int)details.animalId, language));
                }
            }

            return animalResult;
        }

        /// <summary>
        /// Gets all the animals types.
        /// </summary>
        /// <returns>The animals types.</returns>
        public IEnumerable<Animal> GetAllAnimals()
        {
            return zooDB.GetAllAnimals();
        }

        /// <summary>
        /// Gets the AnimalDetials of the animsl with the given id.
        /// </summary>
        /// <param name="animalId">The animal's id.</param>
        /// <returns>The AnimalDetails in all the languages.</returns>
        public IEnumerable<AnimalDetail> GetAllAnimalsDetailById(int animalId)
        {
            //validate the attributes
            //0. check that the animal id exists.
            if (!GetAllAnimals().Any(an => an.id == animalId))
            {
                throw new ArgumentException("Wrong input. The animal id doesn't exists");
            }

            return zooDB.GetAllAnimalsDetails().Where(an => an.animalId == animalId);
        }
        
        /// <summary>
        /// Gets animals by enclosure Id.
        /// </summary>
        /// <param name="encId">The enclosure's Id.</param>
        /// <returns>The animals that are in the enclosure.</returns>
        public IEnumerable<Animal> GetAnimalsByEnclosure(long encId)
        {
            //validate the attributes
            //1. check if the enclosure exists
            if (GetAllEnclosures().SingleOrDefault(en => en.id == encId) == null)
            {
                throw new ArgumentException("Wrong input. The enclosure doesn't exists");
            }

            //get all the animals in the enclosure.
            IEnumerable<Animal> allAnimals = GetAllAnimals().Where(a => a.enclosureId == encId).ToArray();

            return allAnimals;
        }

        /// <summary>
        /// This method adds or updates the animal.
        /// </summary>
        /// <param name="animal">The animal to update.</param>
        public void UpdateAnimal(Animal animal)
        {
            //validate enclosure attributes
            //0. Exists.
            if (animal == default(Animal))
            {
                throw new ArgumentException("No animal given.");
            }

            //1. aniaml name
            if (String.IsNullOrWhiteSpace(animal.name)) 
            {
                throw new ArgumentException("Wrong input. Animal name is null or null");
            }

            //2. enclosure exists
            if (!GetAllEnclosures().ToList().Any(e => e.id == animal.enclosureId))
            {
                throw new ArgumentException("Wrong input. Enclosure id doesn't exists");
            }

            var animals = zooDB.GetAllAnimals();

            if (animal.id == default(int)) //add a new aniaml
            {
                animals.Add(animal);
            }
            else // update existing animal.
            {
                Animal oldAnimal = animals.SingleOrDefault(an => an.id == animal.id);

                //check that the animal exists
                if (oldAnimal == null)
                {
                    throw new ArgumentException("Wrong input. Animal id does'nt exits");
                }

                oldAnimal.name = animal.name;
                oldAnimal.pictureUrl = animal.pictureUrl;
                oldAnimal.preservation = animal.preservation;
                oldAnimal.enclosureId = animal.enclosureId;
            }
        }

        /// <summary>
        /// This method adds or updates the given AnimalDetail.
        /// </summary>
        /// <param name="animalDetails">The object to add or update.</param>
        public void UpdateAnimalDetails(AnimalDetail animalDetails)
        {
            //validate enclosure attributes
            //0. Exists.
            if (animalDetails == default(AnimalDetail))
            {
                throw new ArgumentException("No animal given.");
            }

            //1. language
            if (!ValidLanguage((int)animalDetails.language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }
            
            //2. name
            if (String.IsNullOrWhiteSpace(animalDetails.name))
            {
                throw new ArgumentException("Wrong input. The name is null or whitespace.");
            }

            //3. check that the animal id exists
            if (!GetAllAnimals().Any(a => a.id == animalDetails.animalId))
            {
                throw new ArgumentException("Wrong input. The animal id coesnt exists.");
            }
            var allAnimalDetails = zooDB.GetAllAnimalsDetails();

            var oldDetails = allAnimalDetails.SingleOrDefault(ad => ad.language == animalDetails.language && ad.animalId == animalDetails.animalId);
            
            if (oldDetails == null) //add a new aniamlDetails
            {
                allAnimalDetails.Add(animalDetails);
            }
            else // update existing animal.
            {

                oldDetails.name = animalDetails.name;
                oldDetails.story = animalDetails.story;
                oldDetails.series = animalDetails.series;
                oldDetails.reproduction = animalDetails.reproduction;
                oldDetails.language = animalDetails.language;
                oldDetails.food = animalDetails.food;
                oldDetails.family = animalDetails.family;
                oldDetails.distribution = animalDetails.distribution;
                oldDetails.category = animalDetails.category;
            }
        }

        /// <summary>
        /// Delete the animal.
        /// </summary>
        /// <param name="id">The animal's id to delete.</param>
        public void DeleteAnimal(int id)
        {
            Animal animal = zooDB.GetAllAnimals().SingleOrDefault(a => a.id == id);
            
            //check that the animal exists
            if (animal == null)
            {
                throw new ArgumentException("Wrong input. Animal doesn't exists");
            }

            var animalsToDelete = zooDB.GetAllAnimalsDetails().Where(ad => ad.animalId == animal.id).ToList();

            zooDB.GetAllAnimalsDetails().RemoveRange(animalsToDelete);
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
            //validate the attributes.
            //1. check the lanuguage
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            return zooDB.GetAllPrices().Where(p => p.language == language).ToArray();
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
            if (String.IsNullOrWhiteSpace(price.population))
            {
                throw new ArgumentException("Wrong input. The price population is null or whitespaces");
            }
            
            //2. check that the price amount is valid
            if (price.pricePop < 0)
            {
                throw new ArgumentException("Wrong input, The price amount is lower than 0");
            }

            //3. check the language
            if (!ValidLanguage((int)price.language))
            {
                throw new ArgumentException("Wrong Input. Wrong Language");
            }
            
            var prices = zooDB.GetAllPrices();

            if (price.id == default(int)) //need to add the price.
            {
                if (prices.Any(p => p.population == price.population))
                {
                    throw new ArgumentException("Wrong input while adding price. Price population already exists.");
                }

                prices.Add(price);
            }
            else //update existing price
            {
                var oldPrice = prices.SingleOrDefault(p => p.id == price.id);

                //check that the price exists
                if (oldPrice == null)
                {
                    throw new ArgumentException("Wrong input. The price id doesn't exist");
                }

                //check that if the population changed, the new population doesn't exists.
                if (price.population != oldPrice.population && prices.Any(p => p.population == price.population))
                {
                    throw new ArgumentException("Wrong input while updating price. Price population already exists");
                }

                oldPrice.language = price.language;
                oldPrice.population = price.population;
                oldPrice.pricePop = price.pricePop;
            }
        }

        /// <summary>
        /// Delete the Price elements.
        /// </summary>
        /// <param name="id">The Price's id to delete.</param>
        public void DeletePrice(int id)
        {
            Price price = zooDB.GetAllPrices().SingleOrDefault(p => p.id == id);

            if (price == null)
            {
                throw new ArgumentException("Wrong input. Price doesn't exists.");
            }

            zooDB.GetAllPrices().Remove(price);
        }
        #endregion

        #region OpeningHours
        /// <summary>
        /// Gets all the OpeningHourResults elements - days as strings.
        /// </summary>
        /// <param name="language">The OpeningHour's data language.</param>
        /// <returns>All the OpeningHour elemtents as days as string.</returns>
        public IEnumerable<OpeningHourResult> GetAllOpeningHours (int language)
        {
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            var openingHours = zooDB.GetAllOpeningHours().Where(oh => oh.language == language).ToArray();

            List<OpeningHourResult> opHoursResults = new List<OpeningHourResult>();

            foreach(OpeningHour oh in openingHours)
            {
                opHoursResults.Add(new OpeningHourResult
                {
                    Id = oh.id,
                    Day = Enum.GetName(typeof(Days), oh.day),
                    EndTime = oh.endTime,
                    StartTime = oh.startTime,
                    Language = oh.language
                });
            }

            return opHoursResults;
        }

        /// <summary>
        /// Gets all the OpeningHour elements - days as int.
        /// </summary>
        /// <returns>All the OpeningHour elemtents as days as int.</returns>
        public IEnumerable<OpeningHour> GetAllOpeningHoursType()
        {
            var hebrewLanguage = GetHebewLanguage();
            return zooDB.GetAllOpeningHours().Where(oh => oh.language == hebrewLanguage).ToArray();
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
            if (openingHour.day < 1 || openingHour.day > 7)
            {
                throw new ArgumentException("Wrong input. The day is empty or null");
            }

            //2. check that the end is after the open
            if (openingHour.startTime.CompareTo(openingHour.endTime) > 0)
            {
                throw new ArgumentException("Wrong input. The start time is later than the end time.");
            }

            var openingHours = zooDB.GetAllOpeningHours();

            if (openingHour.id == default(int)) //add a new opening hour
            {
                if (openingHours.Any(oh => oh.day == openingHour.day))
                {
                    throw new ArgumentException("Wrong input while adding Opening hour. The day of the opening hour is already exsists");
                }

                //add the hebrew OpeningHour
                openingHour.language = GetHebewLanguage();
                openingHours.Add(openingHour);

                // add the other 3
                int day = openingHour.day;

                for (int i = 1; i <= 3; i++)
                {
                    var oh = new OpeningHour
                    {
                        day = i * 10 + day,
                        startTime = openingHour.startTime,
                        endTime = openingHour.endTime,
                        language = i+1
                    };

                    openingHours.Add(oh);
                }
            }
            else //update exsist opening hour
            {
                var oldEntity = openingHours.SingleOrDefault(oh => oh.id == openingHour.id);

                if (oldEntity == null)
                {
                    throw new ArgumentException("Wrong input. The opening hour id doesn't exists.");
                }

                var oldHours = openingHours.Where(oh => (oh.day-oldEntity.day) % 10 == 0).ToList();

                oldHours.OrderBy(o => o.language);

                for (int i = 0; i <= 3; i++)
                {

                    if (i >= oldHours.Count())
                    {
                        break;
                    }

                    OpeningHour oldHour = oldHours.ElementAt(i);

                    //check that if the day changed than the new day doesnt exists.
                    if (oldHour.language == GetHebewLanguage() && oldHour.day != openingHour.day && openingHours.Any(o => o.day == openingHour.day))
                    {
                        throw new ArgumentException("Wrong input while updating Opening hour. The Opening hour day already exists");
                    }

                    openingHours.Remove(oldHour);

                    OpeningHour oh = new OpeningHour
                    {
                        day = i * 10 + openingHour.day,
                        startTime = openingHour.startTime,
                        endTime = openingHour.endTime,
                        language = oldHour.language
                    };

                    openingHours.Add(oh);
                }
            }
        }

        /// <summary>
        /// Delete the OpeningHour elements from all the languages.
        /// </summary>
        /// <param name="id">The OpeningHour's id to delete.</param>
        public void DeleteOpeningHour(int id)
        {
            //gets all the OpeningHour elements from the db.
            var allOpeningHours = zooDB.GetAllOpeningHours();

            //get the relevan OpeningHour
            OpeningHour openingHour = allOpeningHours.SingleOrDefault(oh => oh.id == id);
            
            //validate the attributes.
            //1. check that the OpeningHour of the given id exists .
            if (openingHour == null)
            {
                throw new ArgumentException("Wrong input. Opening hour id doesn't exsists.");
            }

            int day = openingHour.day;

            //delete from all the languages
            for (int i = 0; i <= 3; i++)
            {
                //openingHour.day = i * 10 + day;
                var toDelete = allOpeningHours.SingleOrDefault(oh => oh.day == i * 10 + day);
                if (toDelete != null)
                {
                    allOpeningHours.Remove(toDelete);
                }
            }
        }

        #endregion

        #region ContatInfo
        /// <summary>
        /// Gets all the ContactInfos elements in the given language.
        /// </summary>
        /// <param name="language">The ContactInfo's data language.</param>
        /// <returns>All the ContactInfos elemtents.</returns>
        public IEnumerable<ContactInfo> GetAllContactInfos(int language)
        {
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            return zooDB.GetAllContactInfos().Where(ci => ci.language == language).ToArray();
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
            if (String.IsNullOrWhiteSpace(contactInfo.address))
            {
                throw new ArgumentException("Wrong input. ContactInfo's address is whitespaces or null");
            }

            //2. check that the via is valid
            if (String.IsNullOrWhiteSpace(contactInfo.via))
            {
                throw new ArgumentException("Wrong input. ContactInfo's via is whitespaces or null");
            }

            //3. check the language
            if (!ValidLanguage((int)contactInfo.language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            var contactInfos = zooDB.GetAllContactInfos();

            if (contactInfo.id == default(int)) // add a new contact info
            {
                //check that the address and via doesn't exists
                if (contactInfos.Any(ci => ci.via == contactInfo.via && ci.address == contactInfo.address))
                {
                    throw new ArgumentException("Wrong input while adding contactInfo. Contact info address and Via already exists.");
                }

                contactInfos.Add(contactInfo);
            }
            else //update existing contact info
            {
                ContactInfo oldContact = contactInfos.SingleOrDefault(ci => ci.id == contactInfo.id);

                //check that the contact info exists
                if (oldContact == null)
                {
                    throw new ArgumentException("Wrong input. The ContactInfo's id doesn't exists");
                }

                //check that id the via or address change there isn't exsisting via and address
                if ((oldContact.via != contactInfo.via || oldContact.address != contactInfo.address) && contactInfos.Any(ci => ci.address == contactInfo.address && ci.via == contactInfo.via))
                {
                    throw new ArgumentException("Wrong input while updating contactInfo. Contact info address and Via already exists.");
                }

                oldContact.language = contactInfo.language;
                oldContact.via = contactInfo.via;
                oldContact.address = contactInfo.address;
            }
        }

        /// <summary>
        /// Delete the ContactInfo elements.
        /// </summary>
        /// <param name="id">The ContactInfo's id to delete.</param>
        public void DeleteContactInfo(int id)
        {
            //check that the contact info exists.
            ContactInfo contactInfo = zooDB.GetAllContactInfos().SingleOrDefault(ci => ci.id == id);

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

            return zooDB.GetAllSpecialEvents().Where(se => se.language == language).ToArray();
        }

        /// <summary>
        /// Gets SpecialEvent elements between two dates.
        /// returns an element only if all the event is between the given dates
        /// </summary>
        /// <param name="language">The SpecialEvent's data language.</param>
        /// <param name="startDate">The start date to look for</param>
        /// <param name="endDate">The end date to look for</param>
        /// <returns>SpecialEvent elemtents that are within the dates.</returns>
        public IEnumerable<SpecialEvent> GetSpecialEventsByDate(DateTime startDate, DateTime endDate, int language)
        {
            //validate attributes
            //1. validate language
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language.");
            }

            //2. validate the dates.
            if (DateTime.Compare(endDate,startDate) <= 0)
            {
                throw new ArgumentException("Wrong input. the end date is sooner than the start date");
            }

            return zooDB.GetAllSpecialEvents().Where(se => se.language == language && 
                                                     se.startDate >= startDate &&
                                                     se.endDate <= endDate).ToArray();
        }

        /// <summary>
        /// Adds or update the SpecialEvents element.
        /// </summary>
        /// <param name="specialEvent">The SpecialEvent element to add or update.</param>
        /// <param name="isPush"> states if the operation should be sent as puch notification</param>
        public void UpdateSpecialEvent(SpecialEvent specialEvent, bool isPush)
        {
            //validate SpecialEvent attributs
            //0. Exists
            if (specialEvent == default(SpecialEvent))
            {
                throw new ArgumentException("No SpecialEvent was given");
            }

            //1. check that the dates are good
            if (DateTime.Compare(specialEvent.endDate,specialEvent.startDate) < 0)
            {
                throw new ArgumentException("Wrong input. The end date is earlier than the start date.");
            }

            //2. check the language
            if (!ValidLanguage((int)specialEvent.language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            //3. check the title
            if (String.IsNullOrWhiteSpace(specialEvent.title))
            {
                throw new ArgumentException("Wrong input. The title is null or white space");
            }

            //4. check the description
            if (String.IsNullOrWhiteSpace(specialEvent.description))
            {
                throw new ArgumentException("Wrong input. The description is null or white space");
            }

            var specialEvents = zooDB.GetAllSpecialEvents();

            if (specialEvent.id == default(int)) //add a new special event
            {
                //check that the description and title doesn't exists together.
                if (specialEvents.Any(se => se.description == specialEvent.description && se.title == specialEvent.title))
                {
                    throw new ArgumentException("Wrong input while adding a SpecialEvent. The SpecialEvent description already exists");
                }

                specialEvents.Add(specialEvent);
            }
            else //update existing SpecialEvent
            {
                var oldEvent = specialEvents.SingleOrDefault(se => se.id == specialEvent.id);

                //check that the event exists.
                if (oldEvent == null)
                {
                    throw new ArgumentException("Wrong input. The SpecialEvent's id doesn't exists");
                }

                //check that if the description or title changed than they doesn't already exists together
                if ((oldEvent.description != specialEvent.description || oldEvent.title != specialEvent.title) && specialEvents.Any(se => se.description == specialEvent.description && se.title == specialEvent.title))
                {
                    throw new ArgumentException("Wrong input While updating SpecialEvent. The SpecialEvent descroption already exists.");
                }

                oldEvent.description    = specialEvent.description;
                oldEvent.title          = specialEvent.title;
                oldEvent.startDate      = specialEvent.startDate;
                oldEvent.endDate        = specialEvent.endDate;
                oldEvent.imageUrl       = specialEvent.imageUrl;
                oldEvent.language       = specialEvent.language;
            }
            
            if (isPush)
            {
                SendNotificationsAllDevices(specialEvent.title, specialEvent.description);
            }
        }

        /// <summary>
        /// Delete the SpecialEvent elements.
        /// </summary>
        /// <param name="id">The SpecialEvent's id to delete.</param>
        public void DeleteSpecialEvent(int id)
        {
            //check if the SpecialEvent exists
            SpecialEvent specialEvent = zooDB.GetAllSpecialEvents().SingleOrDefault(se => se.id == id);

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

            return zooDB.GetAllWallFeeds().Where(e => e.language == language).ToArray();
        }

        /// <summary>
        /// Adds or Updates a feed wall message.
        /// </summary>
        /// <param name="feed">The wallfeed to add or update</param>
        /// <param name="isPush">represents if the feed should be send as push notification</param>
        /// <param name="isWallFeed">represents if the feed should be added to the wall feed</param>
        public void UpdateWallFeed(WallFeed feed, bool isPush, bool isWallFeed)
        {
            //validate WallFeed attributs
            //0. Exists
            if (feed == default(WallFeed))
            {
                throw new ArgumentException("No wall feed was given");
            }

            //1. check that one of the boolean expressions is true
            if (!isPush && !isWallFeed)
            {
                throw new ArgumentException("The feed should be added to the wall feed or sent as push");
            }

            //2. check the title
            if (String.IsNullOrWhiteSpace(feed.title))
            {
                throw new ArgumentException("Wrong input. The title is null or white space");
            }

            //3. check the info
            if (String.IsNullOrWhiteSpace(feed.info))
            {
                throw new ArgumentException("Wrong input. The info is null or white space");
            }

            //4. check the language
            if (!ValidLanguage((int)feed.language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            var wallFeeds = zooDB.GetAllWallFeeds();


            if (isWallFeed && feed.id == default(int)) //add new feed wall
            {
                //check that the info and title doesn't exists together
                if (wallFeeds.Any(wf => wf.info == feed.info && wf.title == feed.title))
                {
                    throw new ArgumentException("Wrong input while adding WallFeed. The WallFeed info and title are already exists.");
                }

                wallFeeds.Add(feed);
            }
            else //update a feed wall
            {
                WallFeed oldFeed = wallFeeds.SingleOrDefault(wf => wf.id == feed.id);

                //check that the wall feed exists
                if (oldFeed == null)
                {
                    throw new ArgumentException("Wrong input. The WallFeed's id doesn't exists");
                }

                //check that if the info ot title changed than they doesn't already exits together
                if ((oldFeed.info != feed.info || oldFeed.title != feed.title) && wallFeeds.Any(wf => wf.info == feed.info && wf.title == feed.title))
                {
                    throw new ArgumentException("Wrong input while updating WallFeed. The WallFeed Info and title are already exists");
                }

                oldFeed.language = feed.language;
                oldFeed.title = feed.title;
                oldFeed.info = feed.info;
                oldFeed.created = feed.created;
            }

            if (isPush)
            {
                SendNotificationsAllDevices(feed.title, feed.info);
            }
        }

        /// <summary>
        /// Delete a feed wall message.
        /// </summary>
        /// <param name="id">The wallfeed's id to delete</param>
        public void DeleteWallFeed(int id)
        {
            //check that the WallFeed exists
            var wallFeed = zooDB.GetAllWallFeeds().SingleOrDefault(wf => wf.id == id);

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
                .Where(ge => ge.language == language)
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
            if (String.IsNullOrWhiteSpace(info))
            {
                throw new ArgumentException("Wrong input. The info is empty or null");
            }

            //2. validate language
            if (!ValidLanguage(language))
            {
                throw new ArgumentException("Wrong input. Wrong language");
            }

            var generalInfo = zooDB.GetGeneralInfo().SingleOrDefault(gi => gi.language == language);

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
                .Where(ge => ge.language == language)
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

            var generalInfo = zooDB.GetGeneralInfo().SingleOrDefault(gi => gi.language == language);

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
                .Where(ge => ge.language == language)
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

            var generalInfo = zooDB.GetGeneralInfo().SingleOrDefault(gi => gi.language == language);

            generalInfo.contactInfoNote= note;
        }

        /// <summary>
        /// Gets the zoo's map url.
        /// </summary>
        /// <returns>The zoo's url map.</returns>
        public IEnumerable<String> GetMapUrl()
        {
            var language = GetAllLanguages().SingleOrDefault(l => l.name == "עברית").id;

            return zooDB.GetGeneralInfo()
                .Where(ge => ge.language == language)
                .Select(ge => ge.mapBackgroundUrl)
                .ToArray();
        }
        #endregion

        #region Languages
        /// <summary>
        /// Gets all the existing lanuages
        /// </summary>
        /// <returns> All the existing languages.</returns>
        public IEnumerable<Language> GetAllLanguages()
        {
            return zooDB.GetAllLanguages();
        }

        #endregion

        #endregion

        #region Map
        /// <summary>
        /// This method intitiates the map with the given parameters. 
        /// </summary>
        /// <returns> MapSettingResult with all the attributes</returns>
        public MapSettingsResult GetMapSettings()
        {
            var allSettings = zooDB.GetAllMapInfos();

            if (allSettings.Count() == 0)
            {
                throw new ArgumentException("No settings arein the data base");
            }

            //should be only 1.
            var mapSettings = allSettings.First();
            
            return new MapSettingsResult
            {
                PointsPath = mapSettings.pointspath,
                Longitude = mapSettings.longitude,
                Latitude = mapSettings.latitude,
                ZooPointX = mapSettings.zooPointX,
                ZooPointY = mapSettings.zooPointY,
                XLongitudeRatio = mapSettings.xLongitudeRatio,
                YLatitudeRatio = mapSettings.yLatitudeRatio,
                SinAlpha = mapSettings.sinAlpha,
                CosAlpha = mapSettings.cosAlpha,
                MinLatitude = mapSettings.minLatitude,
                MaxLatitude = mapSettings.maxLatitude,
                MinLongitude = mapSettings.minLatitude,
                MaxLongitude = mapSettings.maxLongitude,
                Routes = zooDB.GetAllRoutes().ToArray()
            };
        }
        
        /// <summary>
        /// This method intitiates the map with the given parameters. 
        /// </summary>
        /// <param name="pointsFilePath"> This variables represents the path of the CSV file that contains the points of the map.</param>
        /// <param name="longitude"> This variable represents the longitude of a point in the map</param>
        /// <param name="latitude">This variable represents the latitude of a point in the map</param>
        /// <param name="xLocation"> This variable represents the location of the longitude on the map picture</param>
        /// <param name="yLocation"> This variable represents the location of the latitude on the map picture</param>
        public void InitMapSettings(string pointsFilePath, double longitude, double latitude, int xLocation, int yLocation)
        {
            List<PointMap> points = ExtractPointsFromCSVFile(pointsFilePath);
            Dictionary<PointMap, List<PointMap>> routes = new Dictionary<PointMap, List<PointMap>>();
            double xLongitudeRatio  = 0;
            double yLatitudeRatio   = 0;
            double sinAlpha         = 0;
            double cosAlpha         = 0;
            double minLatitude      = 0;
            double maxLatitude      = 0;
            double minLongitude     = 0;
            double maxLongitude     = 0;

            ///////////// TODO: Aviv's Impl start/////////////
            //Example PointMap usage
            //var point = points.First();
            //int left = point.Left;
            //int right = point.Right;






            ///////////// Aviv's Impl /////////////

            //Add the routes to the db
            //Note: the key of the routes in the db is id which is auto incresed.
            var allRoutes = zooDB.GetAllRoutes();

            foreach(PointMap primaryPoint in routes.Keys)
            {
                foreach(PointMap secPoint in routes[primaryPoint])
                {
                    allRoutes.Add(new Route
                    {
                        primaryLeft     = primaryPoint.Left,
                        primaryRight    = primaryPoint.Right,
                        secLeft         = secPoint.Left,
                        secRight        = secPoint.Right
                    });
                }
            }

            //add the map info to the db.
            zooDB.GetAllMapInfos().Add(new MapInfo
            {
                pointspath      = pointsFilePath,
                longitude       = longitude,
                latitude        = latitude,
                zooPointX       = xLocation,
                zooPointY       = yLocation,
                xLongitudeRatio = xLongitudeRatio,
                yLatitudeRatio  = yLatitudeRatio,
                sinAlpha        = sinAlpha,
                cosAlpha        = cosAlpha,
                minLatitude     = minLatitude,
                maxLatitude     = maxLatitude,
                minLongitude    = minLongitude,
                maxLongitude    = maxLongitude
            });
        }

        /// <summary>
        /// Returns all map markers.
        /// </summary>
        /// <returns>All map markers.s</returns>
        public IEnumerable<MiscMarker> GetAllMarkers()
        {
            // Get all misc markers.
            var miscMarkers     = zooDB.GetAllMiscMarkers().ToArray();

            // Get all enclosure's markers, if exists.
            var enclosuresWithMarkers = zooDB.GetAllEnclosures()
                .Where(enc => enc.markerIconUrl != null && enc.markerLatitude.HasValue && enc.markerLongitude.HasValue)
                .ToArray();

            var enclosureMarkers = enclosuresWithMarkers.Select(enc => new MiscMarker
                {
                    iconUrl     = enc.markerIconUrl,
                    latitude    = (float)enc.markerLatitude.Value,
                    longitude   = (float)enc.markerLongitude.Value
                });

            // Concatenate misc and enclosure markers.
            var allMarkers      = miscMarkers.Concat(enclosureMarkers);

            var allMarkersArray = allMarkers.ToArray();

            return (allMarkersArray);
        }

        #endregion

        #region Users
        /// <summary>
        /// Gets the users.
        /// </summary>
        /// <returns>The users.</returns>
        public IEnumerable<User> GetAllUsers()
        {
            return zooDB.GetAllUsers().ToArray();
        }

        /// <summary>
        /// Connect a worker user to the system.
        /// </summary>
        /// <param name="userName">The wanted user name</param>
        /// <param name="password">The user's password</param>
        /// <returns>a boolean that indicates if the proccess succeded.</returns>
        public bool Login(string userName, string password)
        {
            //check that the user exsits
            User user = GetAllUsers().SingleOrDefault(u => u.name == userName);

            if (user == null)
            {
                return false;
            }

            if (VerifyMd5Hash(password + user.salt, user.password))
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// Gets the user by userName and password.
        /// </summary>
        /// <param name="userName">The User name.</param>
        /// <param name="password">The User password.</param>
        /// <returns>The user.</returns>
        public User GetUserByNameAndPass(string userName, string password)
        {
            //check if the user exists
            var allUsers = zooDB.GetAllUsers().Where(wu => wu.name == userName);

            var user = default(User);

            foreach(User u in allUsers)
            {
                if (VerifyMd5Hash(password + u.salt, u.password))
                {
                    user = u;
                }
            }

            if (user == default(User))
            {
                throw new ArgumentException("Can't find a user with this name and password");
            }

            return user;
        }

        /// <summary>
        /// Updates the User name.
        /// </summary>
        /// <param name="id"> Represents the id of the user that changes the name</param>
        /// <param name="userName"> Represents the new user name that should be saved</param>
        public void UpdateUserName(int id, string userName)
        {
            var allUsers = zooDB.GetAllUsers();

            User user = allUsers.SingleOrDefault(wu => wu.id == id);

            // validate attributes
            // 1. Check that the User exists
            if (user == null)
            {
                throw new ArgumentException("Wrong input. User ID doesn't exists.");
            }

            // 2. check the user name
            if (String.IsNullOrWhiteSpace(userName))
            {
                throw new ArgumentException("Wrong input. The user name is empty or white spaces");
            }

            // 3. check if the user name already exists
            if (allUsers.Any(wu => wu.name == userName))
            {
                throw new ArgumentException("Wrong input . The user name already exists");
            }

            user.name = userName;
        }

        public void UpdateUserPassword(int id, string password)
        {
            var allUsers = zooDB.GetAllUsers();

            User user = allUsers.SingleOrDefault(wu => wu.id == id);

            // validate attributes
            // 1. Check that the User exists
            if (user == null)
            {
                throw new ArgumentException("Wrong input. User ID doesn't exists.");
            }

            // 2. password
            if (String.IsNullOrWhiteSpace(password))
            {
                throw new ArgumentException("Wrong input. The password is empty or white spaces");
            }

            user.salt       = GenerateSalt();
            user.password   = GetMd5Hash(password + user.salt);
        }

        /// <summary>
        /// Updates The User.
        /// </summary>
        /// <param name="userWorker">The UserWorker to add or update.</param>
        public void AddUser(User userWorker)
        {
            //check the attributes
            // 0.Exists
            if (userWorker == default(User))
            {
                throw new ArgumentException("No UserWorker given");
            }

            //TODO: Add an authorization check.

            // 1. Name
            if (String.IsNullOrWhiteSpace(userWorker.name))
            {
                throw new ArgumentException("Wrong input. The user name is empty or white spaces");
            }

            // 2. password
            if (String.IsNullOrWhiteSpace(userWorker.password))
            {
                throw new ArgumentException("Wrong input. The password is empty or white spaces");
            }

            // 3. check the id
            if (userWorker.id != default(int))
            {
                throw new ArgumentException("Wrong input. The user id should set to default");
            }

            var users = zooDB.GetAllUsers();

            //check if the name already exists
            if (users.Any(wu => wu.name == userWorker.name))
            {
                throw new ArgumentException("Wrong input while adding a User. Name already exists");
            }

            userWorker.salt         = GenerateSalt();
            userWorker.password     = GetMd5Hash(userWorker.password + userWorker.salt);
            
            users.Add(userWorker);
        }

        /// <summary>
        /// Delete The User.
        /// </summary>
        /// <param name="id">The User's id to delete.</param>
        public void DeleteUser(int UserId)
        {
            User user = zooDB.GetAllUsers().SingleOrDefault(wu => wu.id == UserId);

            //Check that the User exists
            if (user == null)
            {
                throw new ArgumentException("Wrong input. User ID doesn't exists.");
            }

            zooDB.GetAllUsers().Remove(user);
        }

        #endregion

        #region Notification support

        /// <summary>
        /// get all the devices in the db.
        /// </summary>
        public IEnumerable<Device> GetAllDevices()
        {
            return zooDB.GetAllDevices();
        }

        /// <summary>
        /// updated the status of the given device.
        /// </summary>
        /// <param name="deviceId">The device to update.</param>
        /// <param name="insidePark"> The location status of the device</param>
        public void UpdateDevice(string deviceId, bool insidePark)
        {
            //validate the attribues
            //1. check the device id.
            if (String.IsNullOrWhiteSpace(deviceId))
            {
                throw new ArgumentException("Wrong input. Device Id empty or white spaces.");
            }

            var device = zooDB.GetAllDevices().SingleOrDefault(d => d.deviceId == deviceId);

            //check if the device already exists
            if (device != null)
            {
                device.lastPing = DateTime.Now;
            }
            else
            {
                device = new Device
                {
                    deviceId = deviceId,
                    insidePark = (sbyte)(insidePark? 1 : 0),
                    lastPing = DateTime.Now
                };

                zooDB.GetAllDevices().Add(device);
            }
        }

        /// <summary>
        /// send notification to all the devices
        /// </summary>
        /// <param name="title">The title of the notification.</param>
        /// <param name="body">The body of the notification</param>
        public void SendNotificationsAllDevices(string title, string body)
        {
            var devices = zooDB.GetAllDevices().ToList();

            Task.Factory.StartNew(() => NotifyAsync(title, body, devices));
        }

        /// <summary>
        /// send notification to the online devices
        /// </summary>
        /// <param name="title">The title of the notification.</param>
        /// <param name="body">The body of the notification</param>
        public void SendNotificationsOnlineDevices(string title, string body)
        {
            var devices = zooDB.GetAllDevices().Where(d => d.lastPing.Date.CompareTo(DateTime.Now.Date) == 0 && d.lastPing.AddMinutes(30) > DateTime.UtcNow.ToLocalTime()).ToList();

            Task.Factory.StartNew(() => NotifyAsync(title, body, devices));
        }

        /// <summary>
        /// send notification to the online devices about recurring events if there are at the moment
        /// </summary>
        public void SendNotificationsOnlineDevicesRecurringEvents()
        {
            //get all the recurring events in hebrew
            //TODO: The notification will be sent only in hebrew at the moment.
            //      Need to add a method to get all the recurring events.
            //      But there is a problem with which messeage will be sent to whom
            var allRecEvents = GetAllRecurringEvents(1).ToArray();
            Console.WriteLine("Package received");
            //get the current time
            var currentTime = DateTime.Now;

            Console.WriteLine("Searching for events");
            foreach(RecurringEvent recEve in allRecEvents)
            {
                // get the difference between now and the recEve
                var timeDif = recEve.startTime.Subtract(new TimeSpan(currentTime.Hour, currentTime.Minute, currentTime.Second));
                
                //get the current day of week. add 1 because days start from 0 in c#
                var curDayOfWeek = (long)currentTime.DayOfWeek + 1;

                if (curDayOfWeek == recEve.day && timeDif.Hours == 0 && timeDif.Minutes <= 10 && timeDif.Minutes > TimeSpan.Zero.Minutes)
                {
                    Console.WriteLine("Event found" + recEve.title + ", ", recEve.description);

                    SendNotificationsOnlineDevices(recEve.title, recEve.description);
                }
                else
                {
                    Console.WriteLine("No events found");
                }
            }
        }

        private async void NotifyAsync(string title, string body, List<Device> devices)
        {
            // TODO:: compute whether the users are online or offline and send by that.
            try
            {
                string key = Properties.Settings.Default.serverKey;
                string id = Properties.Settings.Default.senderId;

                // Format the server's key.
                var serverKey = string.Format("key={0}", key);

                // Format the sender's Id.
                var senderId = string.Format("id={0}", id);

                // Get all registration ids from the database.
                var registration_ids = devices.Select(d => d.deviceId).ToArray();

                // Construct the request's data.
                var data = new
                {
                    registration_ids,
                    notification = new { title, body, sound = "default", vibrate = true, background = true }
                };

                var jsonBody = JsonConvert.SerializeObject(data);

                using (var httpRequest = new HttpRequestMessage(HttpMethod.Post, "https://fcm.googleapis.com/fcm/send"))
                {
                    httpRequest.Headers.TryAddWithoutValidation("Authorization", serverKey);
                    httpRequest.Headers.TryAddWithoutValidation("Sender", senderId);
                    httpRequest.Content = new StringContent(jsonBody, Encoding.UTF8, "application/json");

                    using (var httpClient = new HttpClient())
                    {
                        await httpClient.SendAsync(httpRequest);
                    }
                }
            }
            catch (Exception ex)
            {
                // TODO:: LOG.
                throw ex;
            }
        }

        #endregion


        #region private functions

        private bool ValidLanguage(int language)
        {
            return zooDB.GetAllLanguages().SingleOrDefault(l => l.id == language) != null;
        }
        
        private long GetHebewLanguage()
        {
            return GetAllLanguages().SingleOrDefault(l => l.name == "עברית").id;
        }
        
        private bool ValidateTime(RecurringEvent re, RecurringEvent recEvent)
        {
            return  (re.day == recEvent.day) &&
                    ((re.startTime.Subtract(recEvent.startTime) > TimeSpan.Zero &&
                    re.startTime.Subtract(recEvent.endTime) < TimeSpan.Zero)
                    ||
                    (re.endTime.Subtract(recEvent.startTime) > TimeSpan.Zero &&
                    re.endTime.Subtract(recEvent.endTime) < TimeSpan.Zero));
        }

        private Random random = new Random();
        private String GenerateSalt()
        {
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            return new string(Enumerable.Repeat(chars, 32).Select(s => s[random.Next(s.Length)]).ToArray());
        }

        bool VerifyMd5Hash(string input, string hash)
        {
            using (MD5 md5Hash = MD5.Create())
            {
                // Hash the input.
                string hashOfInput = GetMd5Hash(input);

                // Create a StringComparer an compare the hashes.
                StringComparer comparer = StringComparer.OrdinalIgnoreCase;

                if (0 == comparer.Compare(hashOfInput, hash))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        private string GetMd5Hash(string input)
        {
            using (MD5 md5Hash = MD5.Create())
            {
                // Convert the input string to a byte array and compute the hash.
                byte[] data = md5Hash.ComputeHash(Encoding.UTF8.GetBytes(input));

                // Create a new Stringbuilder to collect the bytes
                // and create a string.
                StringBuilder sBuilder = new StringBuilder();

                // Loop through each byte of the hashed data 
                // and format each one as a hexadecimal string.
                for (int i = 0; i < data.Length; i++)
                {
                    sBuilder.Append(data[i].ToString("x2"));
                }

                // Return the hexadecimal string.
                return sBuilder.ToString();
            }
        }

        private List<PointMap> ExtractPointsFromCSVFile(string pointsFilePath)
        {
            //init variables
            string line;
            List<PointMap> points = new List<PointMap>();
            var pointsFileReader = new StreamReader(pointsFilePath);

            //while there is a line to read
            while ((line = pointsFileReader.ReadLine()) != null)
            {
                //seperate the line
                string[] values = Regex.Split(line, ",");

                // parse the string to int
                int left = Int32.Parse(values[0]);
                int right = Int32.Parse(values[1]);

                //create a new point that represented with a Pair object.
                points.Add(new PointMap(left, right));
            }

            return points;
        }
        #endregion

        #region Images

        /// <summary>
        /// Resizes an image.
        /// </summary>
        /// <param name="b">The original bitmap.</param>
        /// <param name="nWidth">new width.</param>
        /// <param name="nHeight">new height.</param>
        /// <returns>A resized image.</returns>
        private Bitmap ResizeImage(Bitmap b, int nWidth, int nHeight)
        {
            Bitmap result = new Bitmap(nWidth, nHeight);

            using (Graphics g = Graphics.FromImage(result))
                g.DrawImage(b, 0, 0, nWidth, nHeight);

            return result;
        }

        /// <summary>
        /// Sets the image's orientation field to default value and flips the image correctly.
        /// </summary>
        /// <param name="b">The bitmap.</param>
        private void SetOrientationToDefault(Bitmap b)
        {
            // 0x112 Is the orientation property that windows uses to orientate the image.
            if (Array.IndexOf(b.PropertyIdList, 274) > -1)
            {
                var orientation = (int)b.GetPropertyItem(274).Value[0];
                switch (orientation)
                {
                    case 1:
                        // No rotation required.
                        break;
                    case 2:
                        b.RotateFlip(RotateFlipType.RotateNoneFlipX);
                        break;
                    case 3:
                        b.RotateFlip(RotateFlipType.Rotate180FlipNone);
                        break;
                    case 4:
                        b.RotateFlip(RotateFlipType.Rotate180FlipX);
                        break;
                    case 5:
                        b.RotateFlip(RotateFlipType.Rotate90FlipX);
                        break;
                    case 6:
                        b.RotateFlip(RotateFlipType.Rotate90FlipNone);
                        break;
                    case 7:
                        b.RotateFlip(RotateFlipType.Rotate270FlipX);
                        break;
                    case 8:
                        b.RotateFlip(RotateFlipType.Rotate270FlipNone);
                        break;
                }
                // This EXIF data is now invalid and should be removed.
                b.RemovePropertyItem(274);
            }
        }

        /// <summary>
        /// Saves the image as icon.
        /// </summary>
        /// <param name="filePath">The file path to save.</param>
        private void SaveAsIcon(string filePath)
        {
            // Get the image from file.
            var image = new Bitmap(filePath);

            // Sets the image orientation to default value.
            SetOrientationToDefault(image);

            // Resize the image.
            var resizedImage = ResizeImage(image, 84, 84);

            var resizedImageWebServer = ResizeImage(image, 24, 24);

            // Dispose the original image file desc.
            image.Dispose();

            // Save the new resized image.
            resizedImage.Save(filePath);

            var filePathExtension = filePath.Substring(filePath.IndexOf('.'));
            var webServerFilePath = filePath.Substring(0, filePath.IndexOf('.')) + "_webServer" + filePathExtension;

            resizedImageWebServer.Save(webServerFilePath);

            resizedImageWebServer.Dispose();

            // Dispose the resized image file desc.
            resizedImage.Dispose();
        }
        
        #endregion

        /// <summary>
        /// post a file to the db
        /// </summary>
        /// <param name="httpRequest">The requested files.</param>
        /// <param name="relativePath">the path.</param>
        /// <returns>An array of the uploaded images path.</returns>
        public JArray FileUpload(HttpRequest httpRequest, string relativePath)
        {
            var fileNames           = new List<String>();

            foreach (string file in httpRequest.Files)
            {
                var postedFile      = httpRequest.Files[file];

                var fileExtension   = postedFile.FileName.Split('.').Last();
                var fileName        = Guid.NewGuid() + "." + fileExtension;

                var filePath        = HttpContext.Current.Server.MapPath(relativePath + fileName);

                postedFile.SaveAs(filePath);

                if (relativePath.Contains("icon") || relativePath.Contains("marker"))
                {
                    SaveAsIcon(filePath);
                }

                fileNames.Add(fileName);
            }

            var responseObject = new JArray();

            foreach (var fn in fileNames)
            {
                responseObject.Add(new JValue(relativePath.Substring(2) + fn));
            }

            return responseObject;
        }

        public void Dispose()
        {
            zooDB.SaveChanges();
        }
    }
}
