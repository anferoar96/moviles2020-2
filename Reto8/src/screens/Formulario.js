import React,{useEffect} from 'react';
import * as SQLite from 'expo-sqlite';
import { StyleSheet,Text, View,TextInput,TouchableHighlight} from 'react-native';
import {Picker} from '@react-native-picker/picker';

export default function Formulario({route,navigation}) {
    const db = SQLite.openDatabase("empresas.db");

    const [info, setInfo] = React.useState({
        nombre: "",
        url: "",
        telefono: "",
        email: "",
        productos: "",
        clasificacion: "Consultoria",
    });
    const [tipo,setTipo]=React.useState("crear")

    const updateInfo = (key, value) => {
        let copyInfo = { ...info };
        copyInfo[key] = value;
        setInfo(copyInfo);
      };

      const addInfoBD = async () => {
        await db.transaction((tx) => {
          tx.executeSql(
            "INSERT INTO company (nombre, url, telefono, email, productos, clasificacion) values (?, ?, ?, ?, ?, ?)",
            [
              info.nombre,
              info.url,
              info.telefono,
              info.email,
              info.productos,
              info.clasificacion,
            ],
            (txObj, resultSet) => console.log(resultSet),
            (txObj, error) => console.log("Error", error)
          );
        });
        navigation.push("Buscar");
      };
    
      const updateInfoBD = async () => {
        await db.transaction((tx) => {
          tx.executeSql(
            "UPDATE company SET nombre = ?, url = ?, telefono = ?, email = ?, productos = ?, clasificacion = ? WHERE id = ?",
            [
              info.nombre,
              info.url,
              info.telefono,
              info.email,
              info.productos,
              info.clasificacion,
              info.id,
            ],
            (txObj, resultSet) => {
              if (resultSet.rowsAffected > 0) {
                console.log(resultSet);
              }
            }
          );
        });
        navigation.push("Buscar");
      };

    useEffect(()=>{
        if(route.params){
            setInfo(route.params);
            setTipo("editar");
        }
    },[])

    return (
    <View style={{flex:1}}>
        <View style={styles.container}>
            <View style={styles.flexRow}>
                <TextInput
                onChangeText={(value) => updateInfo("nombre", value)}
                placeholder="Nombre de la empresa"
                style={styles.input}
                value={info.nombre}
                />
            </View>
            <View style={styles.flexRow}>
                <TextInput
                    onChangeText={(value) => updateInfo("url", value)}
                    placeholder="URL de la empresa"
                    style={styles.input}
                    value={info.url}
                />
            </View>
            <View style={styles.flexRow}>
                <TextInput
                    onChangeText={(value) => updateInfo("telefono", value)}
                    placeholder="Telefono de la empresa"
                    style={styles.input}
                    value={info.telefono}
                />
            </View>
            <View style={styles.flexRow}>
                <TextInput
                    onChangeText={(value) => updateInfo("email", value)}
                    placeholder="Email de la empresa"
                    style={styles.input}
                    value={info.email}
                />
            </View>
            <View style={styles.flexRow}>
                <TextInput
                    onChangeText={(value) => updateInfo("productos", value)}
                    placeholder="Productos de la empresa"
                    style={styles.input}
                    value={info.productos}
                />
            </View>
            <View style={styles.flexRow}>
                <Picker
                    selectedValue={info.clasificacion}
                    style={styles.input}
                    mode="dropdown"
                    onValueChange={(value) => updateInfo("clasificacion", value)}
                    >
                    <Picker.Item label="Consultoria" value="Consultoria" />
                    <Picker.Item label="Desarrollo a la medida" value="Desarrollo a la medida" />
                    <Picker.Item label="Fábrica de software" value="Fabrica de software" />
                </Picker>
            </View>
            {
                tipo=="crear"?(
                    <TouchableHighlight 
                        onPress={()=>{addInfoBD() }}>
                        <Text style={styles.texto}>Crear</Text>
                    </TouchableHighlight> 
                ):(
                    <TouchableHighlight 
                        onPress={()=>{updateInfoBD()}       
                    }>
                        <Text style={styles.texto}>Actualizar</Text>
                    </TouchableHighlight> 
                )
            }
            
        </View>
    </View>
    
    );
}

const styles = StyleSheet.create({

    container: {
        backgroundColor: "#fff",
        flex: 1,
        justifyContent:"center",
        alignItems:"center"
      },
      texto:{
        marginTop:10,
        fontWeight: 'bold',
        color:"green",
        textAlign:"center"
      },
      heading: {
        fontSize: 20,
        fontWeight: "bold",
        textAlign: "center"
      },
      flexRow: {
        flexDirection: "row",
      },
      input: {
        borderColor: "#4630eb",
        borderRadius: 4,
        borderWidth: 1,
        flex:1,
        marginLeft:30,
        marginRight:30,
        marginBottom:10,
        height: 48,
        padding:10
      },
      sectionContainer: {
        marginBottom: 16,
        marginHorizontal: 16
      },
      sectionHeading: {
        fontSize: 18,
        marginBottom: 8
      }
    
})