import React, { useEffect } from 'react';
import * as SQLite from 'expo-sqlite';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

import HomeScreen from './src/screens/HomeScreen';
import Formulario from './src/screens/Formulario';
import Busqueda from './src/screens/Busqueda';

const Stack = createStackNavigator();



export default function App() {
  const db = SQLite.openDatabase("empresas.db");
  useEffect(() => {
    db.transaction(tx => {
      tx.executeSql(
        "create table if not exists company (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, url TEXT, telefono TEXT, email TEXT, productos TEXT, clasificacion TEXT );"
      );
    });
  }, []);

  return (
    <NavigationContainer>
    <Stack.Navigator initialRouteName="HomeScreen">
      <Stack.Screen
        name="Empresas"
        component={HomeScreen}
        options={{headerShown: false}}
      />
      <Stack.Screen
        name="Agregar"
        component={Formulario}
      />
      <Stack.Screen
        name="Buscar"
        component={Busqueda}
      />
    </Stack.Navigator>
  </NavigationContainer>
  );

}

