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

const Logo = () => {
  return (
    <View style={styles.container}>
      <Image
        style={styles.stretch}
        source={require('../images/')}
      />
    </View>
  );
}

export default Logo;