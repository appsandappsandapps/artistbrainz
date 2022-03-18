import SwiftUI
import shared

var searchListVM: SearchListViewModel?
var bookmarksVM: BookmarksViewModel?

@main
struct iOSApp: App {
    init() {
        ServiceLocator()
        searchListVM = SearchListViewModel(
            gotoDetail: { a in }
        )
        bookmarksVM = BookmarksViewModel(
            gotoDetail: { artistId in }
        )
        let uitab = UITabBarItemAppearance()
        uitab.normal.badgeBackgroundColor = .systemBlue
        uitab.selected.badgeBackgroundColor = .systemBlue
        let appearance = UITabBarAppearance()
        appearance.stackedLayoutAppearance = uitab
        appearance.inlineLayoutAppearance = uitab
        appearance.compactInlineLayoutAppearance = uitab
        UITabBar.appearance().standardAppearance = appearance
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
