import { createSlice } from '@reduxjs/toolkit';

export const expenseSlice = createSlice({
  name: 'expenses',
  initialState: {
    expenses: [],
  },
  reducers: {
    setExpenses: (state, action) => {
      state.expenses = action.payload;
    },
    clearExpenses: (state) => {
      state.expenses = []; // Clear expenses on logout
    },
  },
});

// Export the actions
export const { setExpenses, clearExpenses } = expenseSlice.actions;
export default expenseSlice.reducer;
