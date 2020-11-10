import React,{useState,useEffect} from 'react';
import {ScrollView,Modal,View,StyleSheet,TouchableOpacity} from "react-native";
import {Button,Text,Card,CardItem,Body,Container} from "native-base";
import * as SQLite from "expo-sqlite";

function Busqueda({navigation}) {
    const [empresa,setEmpresa]=useState("");
    const [listaEmpresas,setListaEmpresas]=useState([]);
    const [modalV,setModalV]=useState(false);
    const [empresaActual,setEmpresaActual]=useState({});
    const db=SQLite.openDatabase("empresas.db");

    useEffect((tx)=>{
        db.transaction((tx)=>{
            tx.executeSql(
                "select * from company",
                [],
                (txObj,{rows:{_array}})=>setListaEmpresas(_array)
            )
        })
        
    })

    const eliminarEmpresa = async () => {
        await db.transaction((tx) => {
          tx.executeSql(
            "DELETE FROM company WHERE id = ? ",
            [empresaActual.id],
            (txObj, resultSet) => {
              if (resultSet.rowsAffected > 0) {
                let newList = listaEmpresas.filter((empresa) => {
                  if (empresa.id === empresaActual.id) return false;
                  else return true;
                });
                setListaEmpresas(newList);
              }
            }
          );
        });
    
        setModalV(false);
      };

    return (
        <Container>
            <ScrollView>
        {listaEmpresas.map((empresa) => {
          return (
            <Card key={empresa.id}>
              <CardItem>
                <Text>{empresa.nombre}</Text>
              </CardItem>
              <CardItem>
                <Body>
                  <Text>URL: {empresa.url}</Text>
                  <Text>Email: {empresa.email}</Text>
                  <Text>Teléfono: {empresa.telefono}</Text>
                  <Text>Productos y servicios: {empresa.productos}</Text>
                  <Text>Clasificación: {empresa.clasificacion}</Text>
                </Body>
              </CardItem>
              <CardItem>
              <TouchableOpacity
                    onPress={() => {
                        setEmpresaActual(empresa);
                        setModalV(true);
                      }}
                    style={styles.button}
                    >
                    <Text style={styles.text}>
                        Eliminar
                    </Text>
                </TouchableOpacity>
                <TouchableOpacity
                    onPress={() => navigation.navigate("Agregar", empresa)}
                    style={styles.button2}
                    >
                    <Text style={styles.text}>
                        Actualizar
                    </Text>
                </TouchableOpacity>
              </CardItem>
            </Card>
          );
        })}
      </ScrollView> 
      <View>
        <Modal  visible={modalV}>
          <View
            style={{
              margin: 10,
              padding: 35,
              marginTop: 230,
            }}
          >
            <Text>
               Se va a eliminar la empresa {empresaActual.nombre}
            </Text>
            <View
              style={{
                display: "flex",
                justifyContent: "space-between",
                flexDirection: "row",
                marginTop: 10,
              }}
            >
              <TouchableOpacity
                    onPress={() => eliminarEmpresa()}
                    style={styles.button}
                    >
                    <Text style={styles.text}>
                        Confirmar
                    </Text>
                </TouchableOpacity>
                <TouchableOpacity
                    onPress={() => setModalV(false)}
                    style={styles.button1}
                    >
                    <Text style={styles.text}>
                        Cancelar
                    </Text>
                </TouchableOpacity>
            </View>
          </View>
        </Modal>
      </View>
        </Container>
    );
}

export default Busqueda;

const styles = StyleSheet.create({
    button: {
        backgroundColor: '#f05555',
        color: '#ffffff',
        padding: 10,
        height:46,
        marginTop: 16,
        marginLeft: 15,
        borderRadius: 20,
        justifyContent:"center"
      },
      button1:{
        backgroundColor: '#909090',
        color: '#ffffff',
        padding: 10,
        height:46,
        marginTop: 16,
        marginLeft: 15,
        borderRadius: 20,
        justifyContent:"center"
      },
      button2:{
        backgroundColor: '#32CD32',
        color: '#ffffff',
        padding: 10,
        height:46,
        marginTop: 16,
        marginLeft: 15,
        borderRadius: 20,
        justifyContent:"center"
      },
      text: {
        color: '#ffffff',
        marginRight:20,
        marginLeft:20,
        textAlign:"center",
      },
})