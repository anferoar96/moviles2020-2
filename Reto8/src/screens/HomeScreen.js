import React, { useEffect } from 'react';
import { View,TextInput,StyleSheet} from 'react-native';
import Mybutton from '../components/Mybutton';
import {Picker} from '@react-native-picker/picker';

const HomeScreen = ({ navigation }) => {

  const [opcion, setOpcion] = React.useState("Todo")
  
  return (
    <View style={{flex:1}}>
         <View style={{ flex:1,justifyContent:"center",alignItems:"center"}}>
            <View style={{flexDirection:"row"}}>
                <Picker
                    selectedValue={opcion}
                    style={styles.input}
                    mode="dropdown"
                    onValueChange={opcion=>setOpcion(opcion)}
                    >
                    <Picker.Item label="Todo" value="Todo" />
                    <Picker.Item label="Nombre" value="Nombre" />
                    <Picker.Item label="Clasificacion" value="Clasificacion" />
                </Picker>
                <TextInput 
                 style={styles.input}
                />
            </View>
           <View style={{flexDirection:"row"}}>
              <Mybutton
                customClick={()=>navigation.navigate('Agregar')}
                title="Agregar"
              />
              <Mybutton
                customClick={()=>navigation.navigate('Buscar',{selec:opcion})}
                title="Buscar"
            /> 
           </View>
        </View>
    </View>
  );
};

export default HomeScreen;

const styles = StyleSheet.create({
  input: {
    borderColor: "#4630eb",
    borderRadius: 4,
    borderWidth: 1,
    flex:1,
    marginEnd:20,
    marginStart:20,
    marginBottom:10,
    height: 48,
    padding:10
  },
})