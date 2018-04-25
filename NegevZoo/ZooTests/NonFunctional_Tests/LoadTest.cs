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
        //    var numOfRequests = 10;
        //    Task[] task = new Task[numOfRequests];

        //    for (int i = 0; i < task.Count(); i++)
        //    {
        //        task[i] = Task.Factory.StartNew(() => LoadTestMultipleConnectionsHelper());
        //    }

        //    var timer = new Stopwatch();

        //    timer.Start();
        //    Task.WaitAll(task);
        //    timer.Stop();

        //    TimeSpan timeTaken = timer.Elapsed;

        //    Assert.IsTrue(timeTaken.TotalMilliseconds < 9 * 1000);
        //}

        //public void LoadTestMultipleConnectionsHelper()
        //{
        //    HttpClient client = new HttpClient();
        //    client.BaseAddress = new Uri("http://negevZoo.Sytes.net:50555/");
        //    client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
        //    var resp2 = client.GetAsync("enclosures/all/1").Result;
        //    Assert.IsTrue(resp2.IsSuccessStatusCode);
        //}
    }
}
