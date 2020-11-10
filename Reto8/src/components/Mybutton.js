import React from 'react';
import { TouchableOpacity, Text, StyleSheet } from 'react-native';

const Mybutton = (props) => {
  return (
    <TouchableOpacity
      onPress={props.customClick}
      style={styles.button}
      >
      <Text style={styles.text}>
        {props.title}
      </Text>
    </TouchableOpacity>
  );
};

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
  text: {
    color: '#ffffff',
    marginRight:20,
    marginLeft:20,
    textAlign:"center",
  },
});

export default Mybutton;