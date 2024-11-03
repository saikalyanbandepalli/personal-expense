import { configureStore } from '@reduxjs/toolkit';
import expenseReducer from './expenseSlice'; // Your expense slice
import { persistStore, persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage'; // Correct import

const persistConfig = {
  key: 'root',
  storage, // Use local storage
};

const persistedReducer = persistReducer(persistConfig, expenseReducer);

const store = configureStore({
  reducer: {
    expenses: persistedReducer, // Use the persisted reducer
  },
  middleware: (getDefaultMiddleware) => 
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['persist/PERSIST', 'persist/REHYDRATE'], // Ignore these actions
      },
    }),
});

export const persistor = persistStore(store);
export default store;
