
![Logo](https://i.postimg.cc/wBV3DnS5/ic-launcher-round.png)


# BeerBox

Android mobile application for showing a list of beers with filters and search.


## Features

- List of beers
- Filters
- Search
- Beer details


## Tech Stack

**Architecture:** MVI: Immutable states, unidirectional, easy to test and debug, more decoupeled. Pain point: lots of objects for all the states, but benefits outweigh shortcomings.

**Dependencies:** Hilt, Moshi, Coroutines, Paging 3, Navigation, Glide. (detailed usage is commented in gradle file)


## API Reference

#### Get beers list

```http
  GET https://api.punkapi.com/v2/beers?page=${page}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `page`      | `Int` | **Required**. Page number |

#### Get beers list with a beer name filter

```http
  GET https://api.punkapi.com/v2/beers?page=${page}&beer_name=${beerName}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `page`      | `Int` | **Required**. Page number |
| `beerName`      | `string` | **Required**. Beer name |


## Screenshots

|                Splash                          |                 Beer List                         |                   Filtered                     |                   Beer detail                   |
|:----------------------------------------------:|:-------------------------------------------------:|:----------------------------------------------:|:-----------------------------------------------:|
| ![Splash](https://i.postimg.cc/wMfsdn6c/1.png) | ![Beer list](https://i.postimg.cc/kG67BHQy/2.png) | ![Filter](https://i.postimg.cc/QxqjVc5w/3.png) | ![Filter](https://i.postimg.cc/DybbDvWB/5.png)  |

## APK Download

[BeerBox Android App (not a virus :)](https://we.tl/t-KfhgI2UWrL)