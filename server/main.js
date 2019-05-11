// Initialize the default app
var admin = require('firebase-admin');
var app = admin.initializeApp();

var airqualMetricRanges = [];
airqualMetricRanges['o3'] = [100, 180, 240];
airqualMetricRanges['pm10'] = [60, 120, 400];
airqualMetricRanges['pm25'] = [30, 50, 150];
airqualMetricRanges['no2'] = [100, 200, 400];

var fetch = require('node-fetch');
var DOMParser = require('xmldom').DOMParser;

function calculateRiskFor(metric, value) {
  var values = airqualMetricRanges[metric];

  var low = values[0];
  var medium = values[1];
  var high = values[2];

  if (value <= low) {
    return 0;
  } else if (value <= medium) {
    return 1;
  } else if (value <= high) {
    return 2;
  } else {
    return 3;
  }
}

async function getAirquality() {
  var highestRisk = 0;
  await fetch('https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?station=NO0057A')
    .then(res => res.json())
    .then(body => {
      var data = body.data.time;
      for (key in data) {
        if (key <= 24) {
          variables = data[key].variables;

          var o3 = variables.o3_concentration;
          var pm10 = variables.pm10_concentration;
          var pm25 = variables.pm25_concentration;
          var no2 = variables.no2_concentration;

          var o3_risk = calculateRiskFor('o3', o3.value);
          var pm10_risk = calculateRiskFor('pm10', pm10.value);
          var pm25_risk = calculateRiskFor('pm25', pm25.value);
          var no2_risk = calculateRiskFor('no2', no2.value);

          if (o3_risk > highestRisk) {
            console.log(`Higher risk of o3 at ${JSON.stringify(variables)}`);
            console.log(`${data[key].from}`);
            highestRisk = o3_risk;
          }
          if (pm10_risk > highestRisk) {
            console.log(`Higher risk of pm10 at ${JSON.stringify(variables)}`);
            console.log();
            highestRisk = pm10_risk;
          }
          if (pm25_risk > highestRisk) {
            console.log(`Higher risk of pm25 at ${JSON.stringify(variables)}`);
            console.log();
            highestRisk = pm25_risk;
          }
          if (no2_risk > highestRisk) {
            console.log(`Higher risk of no2 at ${JSON.stringify(variables)}`);
            console.log();
            highestRisk = no2_risk;
          }
        }
      }
    })
    .then(res => {
      return highestRisk;
    });

  if (highestRisk === 0) {
    return 'veldig bra';
  } else if (highestRisk === 1) {
    return 'middles';
  } else if (highestRisk === 2) {
    return 'dårlig';
  } else {
    return 'veldig dårlig';
  }
}

async function getUVForecast() {
  today = new Date().toISOString();
  today = today.substring(0, 11) + '12:00:00';

  var uv_value = await fetch(
    `https://in2000-apiproxy.ifi.uio.no/weatherapi/uvforecast/1.0/?time=${today}Z&content_type=text/xml`
  ).then(res =>
    res
      .text()
      .then(body => {
        var uv = new DOMParser().parseFromString(body, 'text/xml');

        var location = uv.getElementsByTagName('location');

        for (let i = 0; i < location.length; i++) {
          const element = location[i];
          if (
            element.getAttributeNode('latitude').nodeValue === '59.75' &&
            element.getAttributeNode('longitude').nodeValue === '10.50'
          ) {
            // Lol, this is just absurdly bad. Trust me, it works!
            return element.childNodes[1].childNodes[7].getAttributeNode('value').nodeValue;
          }
        }
      })
      .then(uv_value => {
        if (uv_value <= 2.9) {
          return 'lav';
        } else if (uv_value <= 5.9) {
          return 'middels';
        } else if (uv_value <= 7.9) {
          return 'hoy';
        } else {
          return 'veldig hoy';
        }
      })
  );
  return uv_value;
}

async function sendNotif() {
  var hour = new Date().getHours();
  if (hour === 7) {
    var airquality = await getAirquality();
    var uvForecast = await getUVForecast();

    const topic = 'weather';
    const message = `Hei, i dag er luftkvaliteten ${airquality} og uv-strålingen ${uvForecast}`;

    console.log(`Sending message to all users: ${message.data.data}`);

    const url = 'https://fcm.googleapis.com/fcm/send';
    await fetch(url, {
      method: 'POST',
      headers: {
        Authorization:
          'key=AAAATRvyT7s:APA91bFVC2kb2h0ReoNIacHBxPtk1E7kdBkOyw46pRKWrgIxmHyN-eKk3EcbBRMWHdPh7QXfQSwZ3DreX5DFCLQVs53BOP8_ByAopIgUqnU_CqBPILebRNSo2jf3oxxl6BSzY6QpeI2-',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        notification: {
          title: 'Daglig rapport',
          body: message
        },
        to: '/topics/weather'
      })
    });
  } else {
    console.log('Dont you dare send notif now lol');
  }
}

setInterval(sendNotif, 1000 * 60 * 60);

