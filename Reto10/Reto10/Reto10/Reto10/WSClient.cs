
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace Reto10
{
    public class WSClient
    {
        public async Task<Root> Get<Root>(string url)
        {
            HttpClient client = new HttpClient();
            var response = await client.GetAsync(url);
            var json = await response.Content.ReadAsStringAsync();
            return JsonConvert.DeserializeObject<Root>(json);
        }
    }
}