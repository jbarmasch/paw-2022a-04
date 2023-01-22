// import {createSlice, PayloadAction} from '@reduxjs/toolkit'
// import {ProductStoreType} from 'types';

// interface CartTypes {
//     cartItems: ProductStoreType[]
// }

// const initialState = {
//     cartItems: []
// } as CartTypes;

// const indexSameProduct = (state: CartTypes, action: ProductStoreType) => {
//     const sameProduct = (events: ProductStoreType) => (
//         events.id === action.id &&
//         events.color === action.color &&
//         events.size === action.size
//     );

//     return state.cartItems.findIndex(sameProduct)
// };

// type AddProductType = {
//     events: ProductStoreType,
//     count: number,
// }

// const cartSlice = createSlice({
//     name: 'cart',
//     initialState,
//     reducers: {
//         addProduct: (state, action: PayloadAction<AddProductType>) => {
//             const cartItems = state.cartItems;

//             // find index of events
//             const index = indexSameProduct(state, action.payload.events);

//             if (index !== -1) {
//                 cartItems[index].count += action.payload.count;
//                 return;
//             }

//             return {
//                 ...state,
//                 cartItems: [...state.cartItems, action.payload.events]
//             };
//         },
//         removeProduct(state, action: PayloadAction<ProductStoreType>) {
//             // find index of events
//             state.cartItems.splice(indexSameProduct(state, action.payload), 1);
//         },
//         setCount(state, action: PayloadAction<AddProductType>) {
//             // find index and add new count on events count
//             const indexItem = indexSameProduct(state, action.payload.events);
//             state.cartItems[indexItem].count = action.payload.count;
//         },
//     },
// })

// export const {addProduct, removeProduct, setCount} = cartSlice.actions
// export default cartSlice.reducer