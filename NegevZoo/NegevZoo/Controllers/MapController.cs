using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;

namespace NegevZoo.Controllers
{
    public class MapController : ControllerBase
    {

        [HttpPost]
        [Route("map/upload")]
        public IHttpActionResult MapImagesUpload()
        {
            var httpRequest = HttpContext.Current.Request;
            if (httpRequest.Files.Count < 1)
            {
                return BadRequest();
            }

            try
            {
                using (var db = GetContext())
                {
                    db.ImagesUpload(httpRequest, @"~/assets/map/misc/");
                    return Ok();
                }
            }
            catch (Exception exp)
            {
                //TODO: add log
                throw new Exception("kaki");
            }
        }
        // TODO:: Under construction.
    }
}
