using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace ZooTests.NonFunctional_Tests
{
    [TestClass]
    public class LoadTest
    {

        //[TestMethod]
        //public void LoadTestMultipleConnections()
        //{
        //    var numOfRequests = 500;

        //    for (int i = 0; i < numOfRequests; i++)
        //    {
        //        var timer = new Stopwatch();

        //        timer.Start();
        //        var resp = Task.Factory.StartNew(() => LoadTestMultipleConnectionsHelper());
        //        timer.Stop();

        //        TimeSpan timeTaken = timer.Elapsed;
                
        //        Assert.AreEqual(0, timeTaken.Seconds);
        //        Assert.IsTrue(timeTaken.Milliseconds < 5);
        //    }
        //}

        //[TestMethod]
        //public void LoadTestMultipleConnectionsHelper()
        //{   
        //    HttpClient client = new HttpClient();
        //    client.BaseAddress = new Uri("http://negevZoo.Sytes.net:50555/");
        //    client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
        //    var resp2 = client.GetAsync("enclosures/all/1").Result;
        //}
    }
}
