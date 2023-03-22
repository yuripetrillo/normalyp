import React from 'react';
import { View, Image, StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  container: {
    paddingTop: 50,
  },
  stretch: {
    width: 125,
    height: 125,
    alignContent:'center',
        margin:25
  },
});

const AddGifImage = () => {
  return (
    <View style={styles.container}>
      <Image
        style={styles.stretch}
        source={require('../images/loading.gif')}
      />
    </View>
  );
}

export default AddGifImage;