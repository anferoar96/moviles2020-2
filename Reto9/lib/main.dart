import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:geolocator/geolocator.dart';
import 'package:latlong/latlong.dart';
import 'dart:math';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  LatLng _center;
  double _latitude = 37.4219983;
  double _longitude = -122.084;
  double _radius = 2000.0;
  MapController _mapController = MapController();
  final myController = TextEditingController();

  @override
  void dispose() {
    myController.dispose();
    super.dispose();
  }

  @override
  void initState() {
    super.initState();
    _getCurrentLocation();
  }

  _getZoomLevel() {
    double _scale = _radius / 400;
    double _zoomLevel = (16 - log(_scale) / log(2));
    return _zoomLevel;
  }

  void _getCurrentLocation() async {
    final position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high);
    print(position);
    setState(() {
      _latitude = position.latitude;
      _longitude = position.longitude;
      if (myController.text != '') {
        var _aux = double.parse(myController.text);
        _radius = _aux * 1000;
      }
      _mapController.move(
          LatLng(position.latitude, position.longitude), _getZoomLevel());
    });
  }

  Widget build(BuildContext context) {
    var circleMarkers = <CircleMarker>[
      CircleMarker(
          point: LatLng(_latitude, _longitude),
          borderColor: Colors.red,
          color: Colors.blue.withOpacity(0.0),
          borderStrokeWidth: 2,
          useRadiusInMeter: true,
          radius: _radius // 2000 meters | 2 km
          )
    ];
    return new Scaffold(
        appBar: AppBar(
          title: Text('Mi mapa'),
          actions: <Widget>[
            Padding(
                padding: EdgeInsets.all(20.0),
                child: Container(
                  width: 100,
                  child: TextField(
                    controller: myController,
                    style: TextStyle(color: Colors.white),
                    decoration: InputDecoration.collapsed(
                      hintText: 'Km',
                      hintStyle: TextStyle(color: Colors.white),
                    ),
                  ),
                )),
            Padding(
                padding: EdgeInsets.only(right: 20.0),
                child: GestureDetector(
                  onTap: () {
                    _getCurrentLocation();
                    // _radius = double.parse(myController.text);
                    /* return showDialog(
                      context: context,
                      builder: (context) {
                        return AlertDialog(
                          content: Text(myController.text),
                        );
                      },
                    ); */
                  },
                  child: Icon(
                    Icons.search,
                    size: 26.0,
                  ),
                )),
          ],
        ),
        body: new FlutterMap(
          //mapController: controller,
          options: new MapOptions(
            center: _center,
            zoom: _getZoomLevel(),
          ),
          mapController: _mapController,
          layers: [
            new TileLayerOptions(
                urlTemplate:
                    "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
                subdomains: ['a', 'b', 'c']),
            new MarkerLayerOptions(
              markers: [
                new Marker(
                  width: 40.0,
                  height: 40.0,
                  point: new LatLng(_latitude, _longitude),
                  builder: (ctx) => new Container(
                    child: Icon(
                      Icons.add_location,
                      color: Colors.red.shade900,
                    ),
                  ),
                ),
              ],
            ),
            new CircleLayerOptions(circles: circleMarkers)
          ],
        ),
        floatingActionButton: Column(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            FloatingActionButton(
              onPressed: () {
                var newZoom = _mapController.zoom + 0.5;
                _mapController.move(_mapController.center, newZoom);
              },
              tooltip: 'Decrement',
              child: Icon(Icons.add),
            ),
            SizedBox(
              height: 10,
            ),
            FloatingActionButton(
              onPressed: () {
                var newZoom = _mapController.zoom - 0.5;
                _mapController.move(_mapController.center, newZoom);
              },
              tooltip: 'Decrement',
              child: Icon(Icons.remove),
            ),
            SizedBox(
              height: 10,
            ),
            FloatingActionButton(
              onPressed: _getCurrentLocation,
              tooltip: 'Location',
              child: Icon(Icons.navigation),
            ),
          ],
        ));
  }
}
