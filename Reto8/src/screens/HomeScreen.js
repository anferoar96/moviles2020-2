import React, { useEffect } from 'react';
import { View,TextInput,StyleSheet} from 'react-native';
import Mybutton from '../components/Mybutton';
import {Picker} from '@react-native-picker/picker';

const HomeScreen = ({ navigation }) => {

  const [info, setInfo] = React.useState({
    res: "",
    opcion: "Todo",
  });

  const updateInfo = (key, value) => {
    let copyInfo = { ...info };
    copyInfo[key] = value;
    setInfo(copyInfo);
  };

  return (
    <View style={{flex:1}}>
         <View style={{ flex:1,justifyContent:"center",alignItems:"center"}}>
            <View style={{flexDirection:"row"}}>
                <Picker
                    selectedValue={info.opcion}
                    style={styles.input}
                    mode="dropdown"
                    onValueChange={(value)=>{
                      updateInfo("res","")
                      updateInfo("opcion",value)}
                    }
                    >
                    <Picker.Item label="Todo" value="Todo" />
                    <Picker.Item label="Nombre" value="Nombre" />
                    <Picker.Item label="Clasificacion" value="Clasificacion" />
                </Picker>
                {
                  info.opcion=="Clasificacion"?(
                    <Picker
                        selectedValue={info.res}
                        style={styles.input}
                        mode="dropdown"
                        onValueChange={(value) => updateInfo("res",value)}
                        >
                        <Picker.Item label="Consultoria" value="Consultoria" />
                        <Picker.Item label="Desarrollo a la medida" value="Desarrollo a la medida" />
                        <Picker.Item label="Fábrica de software" value="Fabrica de software" />
                    </Picker>
                  ):(
                    <TextInput 
                    onChangeText={(value)=>updateInfo("res",value)}
                    style={styles.input}
                    value={info.res}
                    />
                  )
                }
                
            </View>
           <View style={{flexDirection:"row"}}>
              <Mybutton
                customClick={()=>{

                  navigation.navigate('Agregar')
                }}
                title="Agregar"
              />
              <Mybutton
                customClick={()=>navigation.navigate('Buscar',{selec:info.opcion,ans:info.res})}
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